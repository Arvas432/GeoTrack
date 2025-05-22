package com.example.geotrack.ui.social

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.geotrack.R
import com.example.geotrack.domain.public_tracks.Post
import com.example.geotrack.domain.Route
import com.example.geotrack.domain.User
import com.example.geotrack.ui.common_ui_components.HintedLabellessTextField
import com.example.geotrack.ui.common_ui_components.LikeIcon
import com.example.geotrack.ui.common_ui_components.LikedIcon
import com.example.geotrack.ui.common_ui_components.ScreenHeader
import com.example.geotrack.ui.common_ui_components.ValueWithHeader
import com.example.geotrack.ui.robotoFamily
import com.example.geotrack.ui.social.viewmodel.RoutesIntent
import com.example.geotrack.ui.social.viewmodel.RoutesState
import com.example.geotrack.ui.social.viewmodel.RoutesViewModel
import com.example.geotrack.ui.theme.GrayB4
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun RoutesScreen(viewModel: RoutesViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            ScreenHeader(
                stringResource(R.string.routes_screen),
                Modifier.padding(start = 16.dp)
            )
        }
    ) { paddingValues ->
        when(state.value) {
            is RoutesState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                }
            }

            is RoutesState.Error -> Toast.makeText(LocalContext.current,
                stringResource(R.string.Error), Toast.LENGTH_LONG).show()
            is RoutesState.Loaded -> {
                val posts = (state.value as RoutesState.Loaded).posts
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    HintedLabellessTextField(
                        "",
                        "Поиск",
                        {},
                        modifier = Modifier.padding(start = 17.dp, end = 15.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(posts, key = { it.serverId!! }) { post ->
                            PostListItem(post, onLikePost = {
                                post.serverId?.let {
                                    RoutesIntent.RatePost(
                                        it
                                    )
                                }?.let { viewModel.processIntent(it) }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostListItem(post: Post, onLikePost: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .height(350.dp)
            .padding(horizontal = 10.dp)
    ) {
        val (routeTitle,
            profilePicture,
            postParameters,
            postDate,
            postImage) = createRefs()
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile image",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .constrainAs(profilePicture) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .height(42.dp)
                .width(42.dp)
        )
        Text(text = post.name,
            fontFamily = robotoFamily,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.constrainAs(routeTitle) {
                top.linkTo(profilePicture.top)
                start.linkTo(profilePicture.end, margin = 6.dp)
            }
        )
        Text(
            text = post.date.toString(),
            fontFamily = robotoFamily,
            fontSize = 10.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            color = GrayB4,
            modifier = Modifier.constrainAs(postDate) {
                top.linkTo(routeTitle.bottom)
                start.linkTo(routeTitle.start)
            }
        )
        if (post.image != null) {
            Image(
                painter = BitmapPainter(post.image.asImageBitmap()),
                contentDescription = "Route image",
                modifier = Modifier
                    .constrainAs(postImage) {
                        top.linkTo(parent.top, margin = 14.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .height(64.dp)
                    .width(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = "Route image",
                modifier = Modifier
                    .constrainAs(postImage) {
                        top.linkTo(parent.top, margin = 14.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .height(64.dp)
                    .width(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Row(verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(postParameters) {
                    bottom.linkTo(parent.bottom)
                }) {
            ValueWithHeader(stringResource(R.string.avg_speed), buildString {
                append(post.averageSpeed.toString())
                append(
                    stringResource(
                        R.string.kmh
                    )
                )
            }, Modifier.weight(1F))
            ValueWithHeader(stringResource(R.string.distance), buildString {
                append(post.distance.toString())
                append(
                    stringResource(
                        R.string.km
                    )
                )
            }, Modifier.weight(1F))
            ValueWithHeader(
                stringResource(R.string.time),
                post.duration.inWholeMinutes.toString() + " мин",
                Modifier.weight(1F)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .clickable {

                    },
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.likes),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = GrayB4,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Row(
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Image(if (post.liked) LikedIcon else LikedIcon, "Like button", modifier = Modifier.clickable {
                        onLikePost()
                    })
                    Text(
                        text = post.likes.toString(),
                        fontSize = 12.sp,
                        lineHeight = 26.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontWeight = FontWeight.Medium,
                        color = GrayB4,
                    )

                }

            }
        }
    }
}