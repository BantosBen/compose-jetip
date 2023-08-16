package ke.co.banit.jettip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ke.co.banit.jettip.components.InputField
import ke.co.banit.jettip.ui.theme.JetTipTheme
import ke.co.banit.jettip.widgets.FlatIconButton

@OptIn(ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Composable
fun TopHeaderSection(totalPerPerson: Double=0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp)
            .clip(shape = RoundedCornerShape(15.dp)),
        color = Color(0xFFDC8909)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Ksh. $total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )

        }
    }
}


@Preview
@Composable
fun MainContent() {
    BillForm(){billAmount->

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {}
) {
    val splitterState = remember {
        mutableStateOf(1)
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TopHeaderSection()

        Surface(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            shape = CircleShape.copy(all = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(all = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController!!.hide()
                    },

                    )
                //if (validState) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.padding(all = 3.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FlatIconButton(
                            imageVector = Icons.Default.Remove,
                            onClickAction = {
                                if (splitterState.value > 1) splitterState.value -= 1
                            })
                        Text(
                            text = splitterState.value.toString(),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        FlatIconButton(
                            imageVector = Icons.Default.Add,
                            onClickAction = {
                                splitterState.value += 1
                            })
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                    Text(text = "Tip Amount", modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    Text(text = "Ksh.${tipAmountState.value}", modifier = Modifier.align(Alignment.CenterVertically))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercentage%")
                    Spacer(modifier = Modifier.height(12.dp))
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        steps = 5,
                        onValueChangeFinished = {
                            tipAmountState.value =
                                calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentage
                                )
                        })
                }
//            } else {
//                Box() {}
//            }

            }
        }
    }

}

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString()
            .isNotEmpty()
    ) (totalBill * tipPercentage) / 100 else 0.00

}
