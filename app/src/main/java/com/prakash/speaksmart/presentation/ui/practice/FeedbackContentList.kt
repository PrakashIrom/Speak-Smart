package com.prakash.speaksmart.presentation.ui.practice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import com.prakash.speaksmart.presentation.theme.SpeakSmartTheme

@Composable
fun FeedbackContentList(data: SpeechFeedbackResponse) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                ConfidenceSpeedometer(score = data.confidenceScore)
            }
        }

        item {
            TonalFeedbackCard(
                borderColor = SpeakSmartTheme.SurfaceContainerHigh,
                label = "WHAT YOU SAID",
                labelTextQuantityColor = SpeakSmartTheme.OnSurfaceVariant
            ) {
                Text(
                    text = data.originalSentence,
                    style = SpeakSmartTheme.BodyMd,
                    color = SpeakSmartTheme.OnSurface
                )
            }
        }

        item {
            TonalFeedbackCard(
                borderColor = SpeakSmartTheme.EmeraldGreen,
                label = "HOW TO SAY IT PERFECTLY",
                labelTextQuantityColor = SpeakSmartTheme.EmeraldGreen
            ) {
                Text(
                    text = data.correctedSentence,
                    style = SpeakSmartTheme.BodyLg.copy(fontWeight = FontWeight.Medium),
                    color = Color.White
                )
            }
        }

        if (data.grammarMistakes.isNotEmpty()) {
            item {
                Text(
                    text = "Grammar Mistakes",
                    style = SpeakSmartTheme.LabelMd,
                    color = SpeakSmartTheme.OnSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }
            items(data.grammarMistakes) { mistake ->
                TonalFeedbackCard(
                    borderColor = SpeakSmartTheme.PulsingCrimson.copy(alpha = 0.4f),
                    label = mistake.mistake.uppercase(),
                    labelTextQuantityColor = SpeakSmartTheme.PulsingCrimson
                ) {
                    Text(
                        text = mistake.explanation,
                        style = SpeakSmartTheme.BodyMd,
                        color = SpeakSmartTheme.OnSurface
                    )
                }
            }
        }

        val allTips = data.pronunciationTips + data.fluencyTips
        if (allTips.isNotEmpty()) {
            item {
                Text(
                    text = "Fluency & Accent Hacks",
                    style = SpeakSmartTheme.LabelMd,
                    color = SpeakSmartTheme.OnSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }
            items(allTips) { tip ->
                TonalFeedbackCard(
                    borderColor = SpeakSmartTheme.CoolBlue.copy(alpha = 0.4f),
                    label = "COACH TIP",
                    labelTextQuantityColor = SpeakSmartTheme.CoolBlue
                ) {
                    Text(
                        text = tip.tip,
                        style = SpeakSmartTheme.BodyMd,
                        color = SpeakSmartTheme.OnSurface
                    )
                }
            }
        }
    }
}

@Composable
fun TonalFeedbackCard(
    borderColor: Color,
    label: String,
    labelTextQuantityColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(SpeakSmartTheme.RadiusLarge)
            .background(SpeakSmartTheme.SurfaceContainer)
            .border(1.dp, borderColor, SpeakSmartTheme.RadiusLarge)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = label,
                style = SpeakSmartTheme.LabelSm,
                color = labelTextQuantityColor,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
fun ConfidenceSpeedometer(score: Double) {
    val targetSweep = (score * 360f).toFloat()
    val progressColor = when {
        score < 0.5 -> SpeakSmartTheme.PulsingCrimson
        score < 0.8 -> SpeakSmartTheme.CoolBlue
        else -> SpeakSmartTheme.EmeraldGreen
    }

    Box(
        modifier = Modifier.size(130.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(110.dp)) {
            drawCircle(
                color = Color(0xFF252525),
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = targetSweep,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(score * 100).toInt()}%",
                style = SpeakSmartTheme.HeadlineMd.copy(fontSize = 28.sp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "FLUENCY",
                style = SpeakSmartTheme.LabelSm,
                color = SpeakSmartTheme.OnSurfaceVariant
            )
        }
    }
}


