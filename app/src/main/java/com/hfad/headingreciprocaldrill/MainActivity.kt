package com.hfad.headingreciprocaldrill

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.hfad.headingreciprocaldrill.ui.theme.HeadingReciprocalDrillTheme
import java.util.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            onValueChange = { if (it.isDigitsOnly()) recipHdg = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onSend = {
                if (checkAnswer(inputHdg, recipHdg)) { // todo Find a way to factor onSend/onClick action
                    inputHdg = newHeading()
                    recipHdg = ""
                }
            })
        )

        Button(
            onClick = {
                if (checkAnswer(inputHdg, recipHdg)) {
                    inputHdg = newHeading()
                    recipHdg = ""
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
