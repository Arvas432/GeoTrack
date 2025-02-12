package com.example.geotrack.ui.common_ui_components


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SocialIcon: ImageVector
    get() {
        if (_StateLayer1 != null) {
            return _StateLayer1!!
        }
        _StateLayer1 = ImageVector.Builder(
            name = "StateLayer1",
            defaultWidth = 64.dp,
            defaultHeight = 32.dp,
            viewportWidth = 64f,
            viewportHeight = 32f
        ).apply {
            path(fill = SolidColor(Color(0xFFAEAFB4))) {
                moveTo(20f, 24f)
                verticalLineTo(21.225f)
                curveTo(20f, 20.658f, 20.142f, 20.133f, 20.425f, 19.65f)
                curveTo(20.708f, 19.167f, 21.1f, 18.8f, 21.6f, 18.55f)
                curveTo(21.833f, 18.433f, 22.058f, 18.325f, 22.275f, 18.225f)
                curveTo(22.508f, 18.125f, 22.75f, 18.033f, 23f, 17.95f)
                verticalLineTo(24f)
                horizontalLineTo(20f)
                close()
                moveTo(24f, 17f)
                curveTo(23.167f, 17f, 22.458f, 16.708f, 21.875f, 16.125f)
                curveTo(21.292f, 15.542f, 21f, 14.833f, 21f, 14f)
                curveTo(21f, 13.167f, 21.292f, 12.458f, 21.875f, 11.875f)
                curveTo(22.458f, 11.292f, 23.167f, 11f, 24f, 11f)
                curveTo(24.833f, 11f, 25.542f, 11.292f, 26.125f, 11.875f)
                curveTo(26.708f, 12.458f, 27f, 13.167f, 27f, 14f)
                curveTo(27f, 14.833f, 26.708f, 15.542f, 26.125f, 16.125f)
                curveTo(25.542f, 16.708f, 24.833f, 17f, 24f, 17f)
                close()
                moveTo(24f, 15f)
                curveTo(24.283f, 15f, 24.517f, 14.908f, 24.7f, 14.725f)
                curveTo(24.9f, 14.525f, 25f, 14.283f, 25f, 14f)
                curveTo(25f, 13.717f, 24.9f, 13.483f, 24.7f, 13.3f)
                curveTo(24.517f, 13.1f, 24.283f, 13f, 24f, 13f)
                curveTo(23.717f, 13f, 23.475f, 13.1f, 23.275f, 13.3f)
                curveTo(23.092f, 13.483f, 23f, 13.717f, 23f, 14f)
                curveTo(23f, 14.283f, 23.092f, 14.525f, 23.275f, 14.725f)
                curveTo(23.475f, 14.908f, 23.717f, 15f, 24f, 15f)
                close()
                moveTo(24f, 24f)
                verticalLineTo(21.2f)
                curveTo(24f, 20.633f, 24.142f, 20.117f, 24.425f, 19.65f)
                curveTo(24.725f, 19.167f, 25.117f, 18.8f, 25.6f, 18.55f)
                curveTo(26.633f, 18.033f, 27.683f, 17.65f, 28.75f, 17.4f)
                curveTo(29.817f, 17.133f, 30.9f, 17f, 32f, 17f)
                curveTo(33.1f, 17f, 34.183f, 17.133f, 35.25f, 17.4f)
                curveTo(36.317f, 17.65f, 37.367f, 18.033f, 38.4f, 18.55f)
                curveTo(38.883f, 18.8f, 39.267f, 19.167f, 39.55f, 19.65f)
                curveTo(39.85f, 20.117f, 40f, 20.633f, 40f, 21.2f)
                verticalLineTo(24f)
                horizontalLineTo(24f)
                close()
                moveTo(26f, 22f)
                horizontalLineTo(38f)
                verticalLineTo(21.2f)
                curveTo(38f, 21.017f, 37.95f, 20.85f, 37.85f, 20.7f)
                curveTo(37.767f, 20.55f, 37.65f, 20.433f, 37.5f, 20.35f)
                curveTo(36.6f, 19.9f, 35.692f, 19.567f, 34.775f, 19.35f)
                curveTo(33.858f, 19.117f, 32.933f, 19f, 32f, 19f)
                curveTo(31.067f, 19f, 30.142f, 19.117f, 29.225f, 19.35f)
                curveTo(28.308f, 19.567f, 27.4f, 19.9f, 26.5f, 20.35f)
                curveTo(26.35f, 20.433f, 26.225f, 20.55f, 26.125f, 20.7f)
                curveTo(26.042f, 20.85f, 26f, 21.017f, 26f, 21.2f)
                verticalLineTo(22f)
                close()
                moveTo(32f, 16f)
                curveTo(30.9f, 16f, 29.958f, 15.608f, 29.175f, 14.825f)
                curveTo(28.392f, 14.042f, 28f, 13.1f, 28f, 12f)
                curveTo(28f, 10.9f, 28.392f, 9.958f, 29.175f, 9.175f)
                curveTo(29.958f, 8.392f, 30.9f, 8f, 32f, 8f)
                curveTo(33.1f, 8f, 34.042f, 8.392f, 34.825f, 9.175f)
                curveTo(35.608f, 9.958f, 36f, 10.9f, 36f, 12f)
                curveTo(36f, 13.1f, 35.608f, 14.042f, 34.825f, 14.825f)
                curveTo(34.042f, 15.608f, 33.1f, 16f, 32f, 16f)
                close()
                moveTo(32f, 14f)
                curveTo(32.55f, 14f, 33.017f, 13.808f, 33.4f, 13.425f)
                curveTo(33.8f, 13.025f, 34f, 12.55f, 34f, 12f)
                curveTo(34f, 11.45f, 33.8f, 10.983f, 33.4f, 10.6f)
                curveTo(33.017f, 10.2f, 32.55f, 10f, 32f, 10f)
                curveTo(31.45f, 10f, 30.975f, 10.2f, 30.575f, 10.6f)
                curveTo(30.192f, 10.983f, 30f, 11.45f, 30f, 12f)
                curveTo(30f, 12.55f, 30.192f, 13.025f, 30.575f, 13.425f)
                curveTo(30.975f, 13.808f, 31.45f, 14f, 32f, 14f)
                close()
                moveTo(40f, 17f)
                curveTo(39.167f, 17f, 38.458f, 16.708f, 37.875f, 16.125f)
                curveTo(37.292f, 15.542f, 37f, 14.833f, 37f, 14f)
                curveTo(37f, 13.167f, 37.292f, 12.458f, 37.875f, 11.875f)
                curveTo(38.458f, 11.292f, 39.167f, 11f, 40f, 11f)
                curveTo(40.833f, 11f, 41.542f, 11.292f, 42.125f, 11.875f)
                curveTo(42.708f, 12.458f, 43f, 13.167f, 43f, 14f)
                curveTo(43f, 14.833f, 42.708f, 15.542f, 42.125f, 16.125f)
                curveTo(41.542f, 16.708f, 40.833f, 17f, 40f, 17f)
                close()
                moveTo(40f, 15f)
                curveTo(40.283f, 15f, 40.517f, 14.908f, 40.7f, 14.725f)
                curveTo(40.9f, 14.525f, 41f, 14.283f, 41f, 14f)
                curveTo(41f, 13.717f, 40.9f, 13.483f, 40.7f, 13.3f)
                curveTo(40.517f, 13.1f, 40.283f, 13f, 40f, 13f)
                curveTo(39.717f, 13f, 39.475f, 13.1f, 39.275f, 13.3f)
                curveTo(39.092f, 13.483f, 39f, 13.717f, 39f, 14f)
                curveTo(39f, 14.283f, 39.092f, 14.525f, 39.275f, 14.725f)
                curveTo(39.475f, 14.908f, 39.717f, 15f, 40f, 15f)
                close()
                moveTo(41f, 24f)
                verticalLineTo(17.95f)
                curveTo(41.25f, 18.033f, 41.483f, 18.125f, 41.7f, 18.225f)
                curveTo(41.933f, 18.325f, 42.167f, 18.433f, 42.4f, 18.55f)
                curveTo(42.9f, 18.8f, 43.292f, 19.167f, 43.575f, 19.65f)
                curveTo(43.858f, 20.133f, 44f, 20.658f, 44f, 21.225f)
                verticalLineTo(24f)
                horizontalLineTo(41f)
                close()
            }
        }.build()

        return _StateLayer1!!
    }

@Suppress("ObjectPropertyName")
private var _StateLayer1: ImageVector? = null

val MapPin: ImageVector
    get() {
        if (_IconContainer1 != null) {
            return _IconContainer1!!
        }
        _IconContainer1 = ImageVector.Builder(
            name = "IconContainer1",
            defaultWidth = 64.dp,
            defaultHeight = 32.dp,
            viewportWidth = 64f,
            viewportHeight = 32f
        ).apply {
            path(fill = SolidColor(Color(0xFFAEAFB4))) {
                moveTo(32f, 26f)
                curveTo(31.767f, 26f, 31.567f, 25.933f, 31.4f, 25.8f)
                curveTo(31.233f, 25.667f, 31.108f, 25.492f, 31.025f, 25.275f)
                curveTo(30.708f, 24.342f, 30.308f, 23.467f, 29.825f, 22.65f)
                curveTo(29.358f, 21.833f, 28.7f, 20.875f, 27.85f, 19.775f)
                curveTo(27f, 18.675f, 26.308f, 17.625f, 25.775f, 16.625f)
                curveTo(25.258f, 15.625f, 25f, 14.417f, 25f, 13f)
                curveTo(25f, 11.05f, 25.675f, 9.4f, 27.025f, 8.05f)
                curveTo(28.392f, 6.683f, 30.05f, 6f, 32f, 6f)
                curveTo(33.95f, 6f, 35.6f, 6.683f, 36.95f, 8.05f)
                curveTo(38.317f, 9.4f, 39f, 11.05f, 39f, 13f)
                curveTo(39f, 14.517f, 38.708f, 15.783f, 38.125f, 16.8f)
                curveTo(37.558f, 17.8f, 36.9f, 18.792f, 36.15f, 19.775f)
                curveTo(35.25f, 20.975f, 34.567f, 21.975f, 34.1f, 22.775f)
                curveTo(33.65f, 23.558f, 33.275f, 24.392f, 32.975f, 25.275f)
                curveTo(32.892f, 25.508f, 32.758f, 25.692f, 32.575f, 25.825f)
                curveTo(32.408f, 25.942f, 32.217f, 26f, 32f, 26f)
                close()
                moveTo(32f, 15.5f)
                curveTo(32.7f, 15.5f, 33.292f, 15.258f, 33.775f, 14.775f)
                curveTo(34.258f, 14.292f, 34.5f, 13.7f, 34.5f, 13f)
                curveTo(34.5f, 12.3f, 34.258f, 11.708f, 33.775f, 11.225f)
                curveTo(33.292f, 10.742f, 32.7f, 10.5f, 32f, 10.5f)
                curveTo(31.3f, 10.5f, 30.708f, 10.742f, 30.225f, 11.225f)
                curveTo(29.742f, 11.708f, 29.5f, 12.3f, 29.5f, 13f)
                curveTo(29.5f, 13.7f, 29.742f, 14.292f, 30.225f, 14.775f)
                curveTo(30.708f, 15.258f, 31.3f, 15.5f, 32f, 15.5f)
                close()
            }
        }.build()

        return _IconContainer1!!
    }

@Suppress("ObjectPropertyName")
private var _IconContainer1: ImageVector? = null

val Settings: ImageVector
    get() {
        if (_Settings != null) {
            return _Settings!!
        }
        _Settings = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFAEAFB4))) {
                moveTo(9.25f, 22f)
                lineTo(8.85f, 18.8f)
                curveTo(8.633f, 18.717f, 8.429f, 18.617f, 8.238f, 18.5f)
                curveTo(8.046f, 18.383f, 7.858f, 18.258f, 7.675f, 18.125f)
                lineTo(4.7f, 19.375f)
                lineTo(1.95f, 14.625f)
                lineTo(4.525f, 12.675f)
                curveTo(4.508f, 12.558f, 4.5f, 12.446f, 4.5f, 12.337f)
                verticalLineTo(11.663f)
                curveTo(4.5f, 11.554f, 4.508f, 11.442f, 4.525f, 11.325f)
                lineTo(1.95f, 9.375f)
                lineTo(4.7f, 4.625f)
                lineTo(7.675f, 5.875f)
                curveTo(7.858f, 5.742f, 8.05f, 5.617f, 8.25f, 5.5f)
                curveTo(8.45f, 5.383f, 8.65f, 5.283f, 8.85f, 5.2f)
                lineTo(9.25f, 2f)
                horizontalLineTo(14.75f)
                lineTo(15.15f, 5.2f)
                curveTo(15.367f, 5.283f, 15.571f, 5.383f, 15.762f, 5.5f)
                curveTo(15.954f, 5.617f, 16.142f, 5.742f, 16.325f, 5.875f)
                lineTo(19.3f, 4.625f)
                lineTo(22.05f, 9.375f)
                lineTo(19.475f, 11.325f)
                curveTo(19.492f, 11.442f, 19.5f, 11.554f, 19.5f, 11.663f)
                verticalLineTo(12.337f)
                curveTo(19.5f, 12.446f, 19.483f, 12.558f, 19.45f, 12.675f)
                lineTo(22.025f, 14.625f)
                lineTo(19.275f, 19.375f)
                lineTo(16.325f, 18.125f)
                curveTo(16.142f, 18.258f, 15.95f, 18.383f, 15.75f, 18.5f)
                curveTo(15.55f, 18.617f, 15.35f, 18.717f, 15.15f, 18.8f)
                lineTo(14.75f, 22f)
                horizontalLineTo(9.25f)
                close()
                moveTo(11f, 20f)
                horizontalLineTo(12.975f)
                lineTo(13.325f, 17.35f)
                curveTo(13.842f, 17.217f, 14.321f, 17.021f, 14.762f, 16.763f)
                curveTo(15.204f, 16.504f, 15.608f, 16.192f, 15.975f, 15.825f)
                lineTo(18.45f, 16.85f)
                lineTo(19.425f, 15.15f)
                lineTo(17.275f, 13.525f)
                curveTo(17.358f, 13.292f, 17.417f, 13.046f, 17.45f, 12.788f)
                curveTo(17.483f, 12.529f, 17.5f, 12.267f, 17.5f, 12f)
                curveTo(17.5f, 11.733f, 17.483f, 11.471f, 17.45f, 11.212f)
                curveTo(17.417f, 10.954f, 17.358f, 10.708f, 17.275f, 10.475f)
                lineTo(19.425f, 8.85f)
                lineTo(18.45f, 7.15f)
                lineTo(15.975f, 8.2f)
                curveTo(15.608f, 7.817f, 15.204f, 7.496f, 14.762f, 7.238f)
                curveTo(14.321f, 6.979f, 13.842f, 6.783f, 13.325f, 6.65f)
                lineTo(13f, 4f)
                horizontalLineTo(11.025f)
                lineTo(10.675f, 6.65f)
                curveTo(10.158f, 6.783f, 9.679f, 6.979f, 9.238f, 7.238f)
                curveTo(8.796f, 7.496f, 8.392f, 7.808f, 8.025f, 8.175f)
                lineTo(5.55f, 7.15f)
                lineTo(4.575f, 8.85f)
                lineTo(6.725f, 10.45f)
                curveTo(6.642f, 10.7f, 6.583f, 10.95f, 6.55f, 11.2f)
                curveTo(6.517f, 11.45f, 6.5f, 11.717f, 6.5f, 12f)
                curveTo(6.5f, 12.267f, 6.517f, 12.525f, 6.55f, 12.775f)
                curveTo(6.583f, 13.025f, 6.642f, 13.275f, 6.725f, 13.525f)
                lineTo(4.575f, 15.15f)
                lineTo(5.55f, 16.85f)
                lineTo(8.025f, 15.8f)
                curveTo(8.392f, 16.183f, 8.796f, 16.504f, 9.238f, 16.763f)
                curveTo(9.679f, 17.021f, 10.158f, 17.217f, 10.675f, 17.35f)
                lineTo(11f, 20f)
                close()
                moveTo(12.05f, 15.5f)
                curveTo(13.017f, 15.5f, 13.842f, 15.158f, 14.525f, 14.475f)
                curveTo(15.208f, 13.792f, 15.55f, 12.967f, 15.55f, 12f)
                curveTo(15.55f, 11.033f, 15.208f, 10.208f, 14.525f, 9.525f)
                curveTo(13.842f, 8.842f, 13.017f, 8.5f, 12.05f, 8.5f)
                curveTo(11.067f, 8.5f, 10.238f, 8.842f, 9.563f, 9.525f)
                curveTo(8.888f, 10.208f, 8.55f, 11.033f, 8.55f, 12f)
                curveTo(8.55f, 12.967f, 8.888f, 13.792f, 9.563f, 14.475f)
                curveTo(10.238f, 15.158f, 11.067f, 15.5f, 12.05f, 15.5f)
                close()
            }
        }.build()

        return _Settings!!
    }

@Suppress("ObjectPropertyName")
private var _Settings: ImageVector? = null


val LikeIcon: ImageVector
    get() {
        if (_LikeIcon != null) {
            return _LikeIcon!!
        }
        _LikeIcon = ImageVector.Builder(
            name = "LikeIcon",
            defaultWidth = 20.dp,
            defaultHeight = 19.dp,
            viewportWidth = 20f,
            viewportHeight = 19f
        ).apply {
            path(fill = SolidColor(Color(0xFF1D1B20))) {
                moveTo(10f, 19f)
                lineTo(8.55f, 17.7f)
                curveTo(6.867f, 16.183f, 5.475f, 14.875f, 4.375f, 13.775f)
                curveTo(3.275f, 12.675f, 2.4f, 11.692f, 1.75f, 10.825f)
                curveTo(1.1f, 9.942f, 0.642f, 9.133f, 0.375f, 8.4f)
                curveTo(0.125f, 7.667f, 0f, 6.917f, 0f, 6.15f)
                curveTo(0f, 4.583f, 0.525f, 3.275f, 1.575f, 2.225f)
                curveTo(2.625f, 1.175f, 3.933f, 0.65f, 5.5f, 0.65f)
                curveTo(6.367f, 0.65f, 7.192f, 0.833f, 7.975f, 1.2f)
                curveTo(8.758f, 1.567f, 9.433f, 2.083f, 10f, 2.75f)
                curveTo(10.567f, 2.083f, 11.242f, 1.567f, 12.025f, 1.2f)
                curveTo(12.808f, 0.833f, 13.633f, 0.65f, 14.5f, 0.65f)
                curveTo(16.067f, 0.65f, 17.375f, 1.175f, 18.425f, 2.225f)
                curveTo(19.475f, 3.275f, 20f, 4.583f, 20f, 6.15f)
                curveTo(20f, 6.917f, 19.867f, 7.667f, 19.6f, 8.4f)
                curveTo(19.35f, 9.133f, 18.9f, 9.942f, 18.25f, 10.825f)
                curveTo(17.6f, 11.692f, 16.725f, 12.675f, 15.625f, 13.775f)
                curveTo(14.525f, 14.875f, 13.133f, 16.183f, 11.45f, 17.7f)
                lineTo(10f, 19f)
                close()
                moveTo(10f, 16.3f)
                curveTo(11.6f, 14.867f, 12.917f, 13.642f, 13.95f, 12.625f)
                curveTo(14.983f, 11.592f, 15.8f, 10.7f, 16.4f, 9.95f)
                curveTo(17f, 9.183f, 17.417f, 8.508f, 17.65f, 7.925f)
                curveTo(17.883f, 7.325f, 18f, 6.733f, 18f, 6.15f)
                curveTo(18f, 5.15f, 17.667f, 4.317f, 17f, 3.65f)
                curveTo(16.333f, 2.983f, 15.5f, 2.65f, 14.5f, 2.65f)
                curveTo(13.717f, 2.65f, 12.992f, 2.875f, 12.325f, 3.325f)
                curveTo(11.658f, 3.758f, 11.2f, 4.317f, 10.95f, 5f)
                horizontalLineTo(9.05f)
                curveTo(8.8f, 4.317f, 8.342f, 3.758f, 7.675f, 3.325f)
                curveTo(7.008f, 2.875f, 6.283f, 2.65f, 5.5f, 2.65f)
                curveTo(4.5f, 2.65f, 3.667f, 2.983f, 3f, 3.65f)
                curveTo(2.333f, 4.317f, 2f, 5.15f, 2f, 6.15f)
                curveTo(2f, 6.733f, 2.117f, 7.325f, 2.35f, 7.925f)
                curveTo(2.583f, 8.508f, 3f, 9.183f, 3.6f, 9.95f)
                curveTo(4.2f, 10.7f, 5.017f, 11.592f, 6.05f, 12.625f)
                curveTo(7.083f, 13.642f, 8.4f, 14.867f, 10f, 16.3f)
                close()
            }
        }.build()

        return _LikeIcon!!
    }

@Suppress("ObjectPropertyName")
private var _LikeIcon: ImageVector? = null

val Pause: ImageVector
    get() {
        if (_Pause != null) {
            return _Pause!!
        }
        _Pause = ImageVector.Builder(
            name = "Pause",
            defaultWidth = 37.dp,
            defaultHeight = 44.dp,
            viewportWidth = 37f,
            viewportHeight = 44f
        ).apply {
            path(fill = SolidColor(Color(0xFFFDFDFD))) {
                moveTo(24.706f, 43.056f)
                verticalLineTo(0.278f)
                horizontalLineTo(36.908f)
                verticalLineTo(43.056f)
                horizontalLineTo(24.706f)
                close()
                moveTo(0.303f, 43.056f)
                verticalLineTo(0.278f)
                horizontalLineTo(12.504f)
                verticalLineTo(43.056f)
                horizontalLineTo(0.303f)
                close()
            }
        }.build()

        return _Pause!!
    }

@Suppress("ObjectPropertyName")
private var _Pause: ImageVector? = null

val Stop: ImageVector
    get() {
        if (_Stop != null) {
            return _Stop!!
        }
        _Stop = ImageVector.Builder(
            name = "Stop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFFDFDFD))) {
                moveTo(0f, 0f)
                horizontalLineToRelative(24f)
                verticalLineToRelative(24f)
                horizontalLineToRelative(-24f)
                close()
            }
        }.build()

        return _Stop!!
    }

@Suppress("ObjectPropertyName")
private var _Stop: ImageVector? = null


val Abandon: ImageVector
    get() {
        if (_Abandon != null) {
            return _Abandon!!
        }
        _Abandon = ImageVector.Builder(
            name = "Abandon",
            defaultWidth = 28.dp,
            defaultHeight = 30.dp,
            viewportWidth = 28f,
            viewportHeight = 30f
        ).apply {
            path(fill = SolidColor(Color(0xFFFDFDFD))) {
                moveTo(5.667f, 30f)
                curveTo(4.75f, 30f, 3.965f, 29.674f, 3.312f, 29.021f)
                curveTo(2.66f, 28.368f, 2.333f, 27.583f, 2.333f, 26.667f)
                verticalLineTo(5f)
                horizontalLineTo(0.667f)
                verticalLineTo(1.667f)
                horizontalLineTo(9f)
                verticalLineTo(0f)
                horizontalLineTo(19f)
                verticalLineTo(1.667f)
                horizontalLineTo(27.333f)
                verticalLineTo(5f)
                horizontalLineTo(25.667f)
                verticalLineTo(26.667f)
                curveTo(25.667f, 27.583f, 25.34f, 28.368f, 24.688f, 29.021f)
                curveTo(24.035f, 29.674f, 23.25f, 30f, 22.333f, 30f)
                horizontalLineTo(5.667f)
                close()
                moveTo(22.333f, 5f)
                horizontalLineTo(5.667f)
                verticalLineTo(26.667f)
                horizontalLineTo(22.333f)
                verticalLineTo(5f)
                close()
                moveTo(9f, 23.333f)
                horizontalLineTo(12.333f)
                verticalLineTo(8.333f)
                horizontalLineTo(9f)
                verticalLineTo(23.333f)
                close()
                moveTo(15.667f, 23.333f)
                horizontalLineTo(19f)
                verticalLineTo(8.333f)
                horizontalLineTo(15.667f)
                verticalLineTo(23.333f)
                close()
            }
        }.build()

        return _Abandon!!
    }

@Suppress("ObjectPropertyName")
private var _Abandon: ImageVector? = null
