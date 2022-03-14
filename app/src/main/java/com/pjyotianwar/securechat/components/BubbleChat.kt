package com.pjyotianwar.securechat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.pjyotianwar.securechat.convertLongToTime
import com.pjyotianwar.securechat.decrypt
import com.pjyotianwar.securechat.models.Chats

@Composable
fun UserBubble(message: Chats, sdrImg: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp, end = 10.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {

                    Column(modifier = Modifier.weight(4f, true).padding(4.dp)) {
                        Text(
                            text = decrypt(message.message),
                            fontSize = 16.sp
                        )
                        Text(
                            text = convertLongToTime(message.createdAt),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Right, modifier = Modifier.align(Alignment.End),
                            color = MaterialTheme.colors.onSecondary.copy(alpha = 0.6f)
                        )
                    }

                Image(
                    painter = rememberImagePainter(
                        data = sdrImg?.toUri(),
                        builder = {
                            transformations(CircleCropTransformation())
                        }),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .aspectRatio(1f)
                )
            }
    }
}

@Composable
fun PeerBubble(message: Chats, rcvrImg: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 80.dp)
                .clip(RoundedCornerShape(12.dp)),
        ) {

            Row(modifier = Modifier
                .align(Alignment.TopStart)
                .background(MaterialTheme.colors.secondaryVariant.copy(alpha = 0.6f))
                .padding(all = 8.dp)
            )
            {
                Image(
                    painter = rememberImagePainter(
                        data = rcvrImg?.toUri(),
                        builder = {
                            transformations(CircleCropTransformation())
                        }),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .aspectRatio(1f)
                )

                Column(
                    modifier = Modifier.weight(4f, true).padding(4.dp)
                ) {
                    Text(
                        text = decrypt(message.message),
                        fontSize = 16.sp
                    )
                    Text(
                        text = convertLongToTime(message.createdAt),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.align(Alignment.End),
                        color = MaterialTheme.colors.onSecondary.copy(alpha = 0.6f)
                    )
                }
            }
    }
}