@file:Suppress("DEPRECATION")

package com.example.droopy.video.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.TextureView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtm.*
import kotlinx.coroutines.launch

private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun VideoScreen(viewModel: VideoViewModel) {
    val videoToken: String by viewModel.videoToken.observeAsState(initial = "")
    val chatToken: String by viewModel.chatToken.observeAsState(initial = "")
    val channelName: String by viewModel.channel.observeAsState(initial = "")
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
        UIRequirePermissions(
            permissions = permissions,
            onPermissionGranted = {
                if(videoToken == "") {
                    val coroutineScope = rememberCoroutineScope()
                    coroutineScope.launch {
                        viewModel.onVideoInitialized("1")
                    }
                }
                if(videoToken != "") {
                    ChatModule(chatToken, channelName)
                    //CallScreen(videoToken, channelName)
                } else {
                    if(isLoading) {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }
                }
            },
            onPermissionDenied = {
                AlertScreen(it)
            }
        )
}

@Composable
private fun ChatModule(chatToken: String, channelName: String) {
    Log.d(TAG, "CHAT MODULE")
    Log.d(TAG, "JOINING $chatToken, CHANNEL $channelName")
    val context = LocalContext.current
    val chatEngine: RtmClient = remember{
     initChatEngine(context, object: RtmClientListener {
         override fun onMessageReceived(message: RtmMessage?, peer: String?) {
             TODO("Not yet implemented")
         }

         override fun onTokenExpired() {
             TODO("Not yet implemented")
         }

         override fun onTokenPrivilegeWillExpire() {
             TODO("Not yet implemented")
         }

         override fun onConnectionStateChanged(newState: Int, oldState: Int) {
             TODO("Not yet implemented")
         }

         override fun onPeersOnlineStatusChanged(p0: MutableMap<String, Int>?) {
             TODO("Not yet implemented")
         }
     }, channelName, "1", chatToken)
    }
}

@Composable
private fun CallScreen(videToken: String, channelName: String) {
    val context = LocalContext.current
    Log.d(TAG, "Initializing calling screen")

    val localSurfaceView: TextureView? by remember {
        mutableStateOf(RtcEngine.CreateTextureView(context))
    }

    var remoteUserMap by remember {
        mutableStateOf(mapOf<Int, TextureView?>())
    }

    val videoEngine = remember {
        initVideoEngine(context, object : IRtcEngineEventHandler() {
            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                Log.d(TAG, "channel joined::::$channel,uid:$uid,elapsed:$elapsed")
            }

            override fun onError(err: Int) {
                super.onError(err)
                Log.d(TAG, "error::: $err")
            }

            override fun onUserJoined(uid: Int, elapsed: Int) {
                Log.d(TAG, "onUserJoined:$uid")
                val desiredUserList = remoteUserMap.toMutableMap()
                desiredUserList[uid] = null
                remoteUserMap = desiredUserList.toMap()
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                Log.d(TAG, "onUserOffline:$uid")
                val desiredUserList = remoteUserMap.toMutableMap()
                desiredUserList.remove(uid)
                remoteUserMap = desiredUserList.toMap()
            }


        }, channelName, "Broadcaster", videToken)
    }


    videoEngine.setupLocalVideo(VideoCanvas(localSurfaceView, Constants.RENDER_MODE_FIT, 0))
    Box(Modifier.fillMaxSize()) {
        localSurfaceView?.let { local ->
            AndroidView(factory = { local }, Modifier.fillMaxSize())
        }
        RemoteView(remoteListInfo = remoteUserMap, mEngine = videoEngine)
        UserControls(videoEngine = videoEngine)
    }

}

@Composable
private fun RemoteView(remoteListInfo: Map<Int, TextureView?>, mEngine: RtcEngine) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.2f)
            .horizontalScroll(state = rememberScrollState())
    ) {
        remoteListInfo.forEach { entry ->
            val remoteTextureView =
                RtcEngine.CreateTextureView(context).takeIf { entry.value == null }?:entry.value
            AndroidView(
                factory = { remoteTextureView!! },
                modifier = Modifier.size(Dp(180f), Dp(240f))
            )
            mEngine.setupRemoteVideo(
                VideoCanvas(
                    remoteTextureView,
                    Constants.RENDER_MODE_HIDDEN,
                    entry.key
                )
            )
        }
    }
}

fun initVideoEngine(current: Context, eventHandler: IRtcEngineEventHandler, channelName: String, userRole: String, token: String): RtcEngine =
    RtcEngine.create(current, "a997ba8743d44cf8bc3bec156c7fe7f1", eventHandler).apply {
        enableVideo()
        setChannelProfile(1)
        if (userRole == "Broadcaster") {
            setClientRole(1)
        } else {
            setClientRole(0)
        }
        joinChannel(token, channelName, "", 0)
    }

fun initChatEngine(current: Context, eventListener: RtmClientListener, channelName: String, userId: String, chatToken: String): RtmClient =
    RtmClient.createInstance(current, "a997ba8743d44cf8bc3bec156c7fe7f1", eventListener).apply{
        Log.d(TAG, "CHAT TOKEN $chatToken USER: $userId")
        login(chatToken, userId, object: ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                Log.d(TAG, "SUCCESS LOGIN $p0")
                TODO("Not yet implemented")
            }

            override fun onFailure(p0: ErrorInfo?) {
                Log.d(TAG, "FAILED $p0")
                TODO("Not yet implemented")
            }
        })
        var channel = createChannel("chat-$channelName", object: RtmChannelListener {
            override fun onAttributesUpdated(p0: MutableList<RtmChannelAttribute>?) {
                TODO("Not yet implemented")
            }

            override fun onMemberCountUpdated(p0: Int) {
                TODO("Not yet implemented")
            }

            override fun onMemberJoined(p0: RtmChannelMember?) {
                Log.d(TAG,"Member joined $p0")
                TODO("Not yet implemented")
            }

            override fun onMemberLeft(p0: RtmChannelMember?) {
                TODO("Not yet implemented")
            }

            override fun onMessageReceived(p0: RtmMessage?, p1: RtmChannelMember?) {
                Log.d(TAG, "Message received ${p0?.text}")
                TODO("Not yet implemented")
            }
        }).join(object: ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                Log.d(TAG, "JOINED SUCCESS $p0")
            }

            override fun onFailure(p0: ErrorInfo?) {
                Log.d(TAG, "JOINED FAILED $p0")
            }
        })
    }

@Composable
private fun UserControls(videoEngine: RtcEngine) {
    var muted by remember { mutableStateOf(false) }
    var videoDisabled by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        Arrangement.SpaceEvenly,
        Alignment.Bottom
    ) {
        OutlinedButton(
            onClick = {
                muted = !muted
                videoEngine.muteLocalAudioStream(muted)
            },
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = if (muted) Color.Blue else Color.White)
        ) {
            if (muted) {
                Icon(Icons.Rounded.MicOff, contentDescription = "Tap to unmute mic", tint = Color.White)
            } else {
                Icon(Icons.Rounded.Mic, contentDescription = "Tap to mute mic", tint = Color.Blue)
            }
        }
        OutlinedButton(
            onClick = {
                videoEngine.leaveChannel()
                activity?.finish()
            },
            shape = CircleShape,
            modifier = Modifier.size(70.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Red)
        ) {
            Icon(Icons.Rounded.CallEnd, contentDescription = "Tap to disconnect Call", tint = Color.White)

        }
        OutlinedButton(
            onClick = {
                videoDisabled = !videoDisabled
                videoEngine.muteLocalVideoStream(videoDisabled)
            },
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = if (videoDisabled) Color.Blue else Color.White)
        ) {
            if (videoDisabled) {
                Icon(Icons.Rounded.VideocamOff, contentDescription = "Tap to enable Video", tint = Color.White)
            } else {
                Icon(Icons.Rounded.Videocam, contentDescription = "Tap to disable Video", tint = Color.Blue)
            }
        }
    }
}
@Composable
private fun AlertScreen(requester: () -> Unit) {
    val context = LocalContext.current

    Log.d(TAG, "AlertScreen")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            requestPermissions(
                context as Activity,
                permissions,
                22
            )
            requester()
        }) {
            Icon(Icons.Rounded.Warning, "Permission Required")
            Text(text = "Permission Required")
        }
    }
}

/**
 * Helper Function for Permission Check
 */
@Composable
private fun UIRequirePermissions(
    permissions: Array<String>,
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable (requester: () -> Unit) -> Unit
) {
    Log.d(TAG, "UIRequirePermissions")
    val context = LocalContext.current

    var grantState by remember {
        mutableStateOf(permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        })
    }

    if (grantState) {
        onPermissionGranted()
    } else {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = {
                grantState = !it.containsValue(false)
            }
        )
        onPermissionDenied {
            Log.d(TAG, "launcher.launch")
            launcher.launch(permissions)
        }
    }
}