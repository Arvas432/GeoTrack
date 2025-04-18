package com.example.geotrack.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PlaceholderTransformation(val placeholder: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return placeholderFilter(text, placeholder)
    }
}

fun placeholderFilter(text: AnnotatedString, placeholder: String): TransformedText {

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return 0
        }

        override fun transformedToOriginal(offset: Int): Int {
            return 0
        }
    }

    return TransformedText(AnnotatedString(placeholder), numberOffsetTranslator)
}