package com.hecosw.trailtracker.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

object AspectRatioModifier {

    fun Modifier.aspectRatioOneToOne() = layout { measurable, constraints ->
        val size = constraints.maxWidth
        val newConstraints = constraints.copy(minWidth = size, maxWidth = size, minHeight = size, maxHeight = size)
        val placeable = measurable.measure(newConstraints)
        layout(size, size) {
            placeable.placeRelative(0, 0)
        }
    }

    fun Modifier.aspectRatioFourToThree() = layout { measurable, constraints ->
        val width = constraints.maxWidth
        val height = width * 3 / 4
        val newConstraints = constraints.copy(minWidth = width, maxWidth = width, minHeight = height, maxHeight = height)
        val placeable = measurable.measure(newConstraints)
        layout(width, height) {
            placeable.placeRelative(0, 0)
        }
    }

    fun Modifier.aspectRatioSixteenToNine() = layout { measurable, constraints ->
        val width = constraints.maxWidth
        val height = width * 9 / 16
        val newConstraints = constraints.copy(minWidth = width, maxWidth = width, minHeight = height, maxHeight = height)
        val placeable = measurable.measure(newConstraints)
        layout(width, height) {
            placeable.placeRelative(0, 0)
        }
    }

}