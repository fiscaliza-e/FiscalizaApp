package br.edu.ifal.fiscalizaapp.screens.example.composables.exampleComposable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.edu.ifal.fiscalizaapp.composables.protocolcard.ProtocolCard

val data = "I could pee on this if i had the energy spill litter box, scratch at owner, destroy all furniture, especially couch and i cry and cry and cry unless you pet me, and then maybe i cry just for fun open the door, let me out, let me out, let me-out, let me-aow, let meaow, meaow!. Nyaa nyaa kitty run to human with blood on mouth from frenzied attack on poor innocent mouse, don't i look cute?. X catch small lizards, bring them into house, then unable to find them on carpet kitty kitty pussy cat doll yet sit in window and stare oooh, a bird, yum lasers are tiny mice and litter kitter kitty litty little kitten big roar roar feed me. To pet a cat, rub its belly, endure blood and agony, quietly weep, keep rubbing belly cereal boxes make for five star accommodation . Intrigued by the shower walk on keyboard . Cat milk copy park pee walk owner escape bored tired cage droppings sick vet vomit caticus cuteicus, the best thing in the universe is a cardboard box. My water bowl is clean and freshly replenished, so i'll drink from the toilet lick the curtain just to be annoying i hate cucumber pls dont throw it at me tuxe"

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewProtocolCard(){
    val sampleDate = "10-22-2025"
    ProtocolCard("Pavimentação", data, "Resolvido", "283748234", sampleDate, modifier = Modifier)
}