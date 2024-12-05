package com.hfad.headingreciprocaldrill

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.hfad.headingreciprocaldrill.ui.theme.HeadingReciprocalDrillTheme
import java.util.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val am : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        enableEdgeToEdge()
        setContent {
            HeadingReciprocalDrillTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HdgRecipDrill(modifier = Modifier.padding(innerPadding) )
                }
            }
        }
    }
}

fun newHeading(): String {
    val randomizer: Random = Random(System.currentTimeMillis())
    val hdg: Int = (randomizer.nextInt(36) + 1)
    return hdg.toString()
}

@Composable
fun HdgRecipDrill(modifier: Modifier = Modifier, hdg: String = newHeading(), recip: String = "") {
    //HdgRecipControls(modifier = modifier, hdg = hdg, recip = recip)
    HdgRecipControls(hdg, recip, modifier)
}

@Composable
fun HdgRecipControls(hdg: String, recip: String, modifier: Modifier) {
    var inputHdg by rememberSaveable { mutableStateOf(hdg) }
    var recipHdg by rememberSaveable { mutableStateOf(recip) }
    val context = LocalContext.current

    fun correctAnswerReset() {
        Log.d("correctAnswerReset", "called")
        inputHdg = newHeading()
        recipHdg = ""
    }

    Column {

        TextField(
            value=inputHdg,
            label = {Text(text = "Heading:",modifier = modifier)},
            onValueChange = { inputHdg = it },
            singleLine = true,
            readOnly = true,)

        TextField(
            value=recipHdg,
            label = {Text(text = "Reciprocal:", modifier = modifier)},
            onValueChange = {
                Log.v("onValueChange", "CALLED")
                if (it.isDigitsOnly()) recipHdg = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    if (checkAnswer(inputHdg, recipHdg)) {
                        correctAnswerReset()
                    }
                }
            ),
        )

        Button(
            onClick = {
                if (checkAnswer(inputHdg, recipHdg)) {
                    correctAnswerReset()
                }
                else {
                    val soundPool = SoundPool.Builder()
                    soundPool.setContext(context)
                    //soundPool.load //TODO: figure out how to reference /raw resources
                    val toneGen = ToneGenerator(AudioManager.STREAM_SYSTEM, 50) // TODO: get proper volume level
                    toneGen.startTone(ToneGenerator.TONE_DTMF_0, 500)
                }
            }
        ) {
            Text("Check")
        }
        //Button(onClick = {checkAnswer()})
    }

}

fun checkAnswer(hdg1s: String, hdg2s: String): Boolean {
    var hdg1: Int
    var hdg2: Int
    try { hdg1 = hdg1s.toInt() }
    catch (e: NumberFormatException) {
        Log.d("checkAnswer", "NumberFormatException caught")
        hdg1 = 0
    }
    try {hdg2 = hdg2s.toInt()}
    catch (e: NumberFormatException) {
        Log.d("checkAnswer", "NumberFormatException caught")
        hdg2 = 0
    }

    val isRecipText: String
    val isRecip: Boolean = (recip(hdg1) == hdg2)
    if (isRecip) {
        isRecipText = "are reciprocals"
    }
    else
        isRecipText = "are not reciprocals"

    Log.v("checkAnswer", "$hdg1 and $hdg2 $isRecipText")

    return isRecip
}

@Preview(showBackground = true)
@Composable
fun HdgRecipDrillPreview() {
    HeadingReciprocalDrillTheme {
        HdgRecipDrill(hdg = "36", recip = "18")
    }
}


fun recip (hdg: Int) : Int {
    var r: Int = (hdg + 18) % 36
    if (r == 0) {
        r = 36
    }
    return r
}
