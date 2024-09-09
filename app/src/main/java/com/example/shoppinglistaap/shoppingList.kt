package com.example.shoppinglistaap

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItems(
    var id:Int,
    var name:String,
    var quantity:String,
    var isEditing:Boolean = false

)



@Composable
fun ShoppingListItems(
    item: ShoppingItems,
    editButton:()-> Unit,  //unit stands for void  here or no value
    deleteButton:()->Unit
) {
    Column (){


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    border = BorderStroke(2.dp, Color.Blue),
                    shape = RoundedCornerShape(20),
                )
        ) {
            Text(text = item.name, modifier = Modifier.padding(2.dp))
            Text(text = " Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.size(80.dp))
            Row() {
                IconButton(onClick = { editButton() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)

                }
                IconButton(onClick = { deleteButton() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)

                }
            }

        }



    }
}

@Composable
fun ShoppingList() {

    var sItem by remember { mutableStateOf(listOf<ShoppingItems>()) }
    var showDailog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf(" ") }
    var itemQty by remember { mutableStateOf( " ") }


    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = {
            showDailog = true

        }) {
            Text(text = "ADD LIST")

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItem){
                    item->
                if(item.isEditing){
                    ShoppingEditItems(items = item, onEditComplete = {
                            editedName,editedQty->
                        sItem=sItem.map { it.copy(isEditing = false)}
                        val editedItem=sItem.find { it.id==item.id }
                        editedItem?.let{
                            it.name =editedName
                            it.quantity= editedQty.toString()
                        }
                    })
                }else{
                    ShoppingListItems(item = item,
                        editButton = {
                            sItem=sItem.map { it.copy(isEditing =it.id==item.id ) }
                        },
                        deleteButton = {
                            sItem=sItem-item
                        }

                    )
                }

            }
        }
        if (showDailog) {
            AlertDialog(
                onDismissRequest = { showDailog=false},
                confirmButton = { Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ){

                    Button(onClick = {
                        if(itemName.isNotEmpty()){
                            val  newItem =ShoppingItems(
                                id = sItem.size +1,
                                name=itemName,
                                quantity = itemQty
                            )
                            sItem=  sItem + newItem
                            showDailog=false
                            itemName=" "

                        }}){
                        Text(text = "Add")
                    }

                    Button(onClick = {

                        showDailog=false

                    }) {
                        Text(text = "Cancel")
                    }




                } },
                title = { Text(text = "ShoppingList") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange ={itemName=it},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = itemQty,
                            onValueChange ={itemQty=it},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true
                        )


                    }
                }
            )




        }
    }
}





@Composable
fun ShoppingEditItems( items:ShoppingItems,onEditComplete:(String,String)->Unit){
    var editedItem by remember { mutableStateOf(" ") }
    var editedQty by  remember{ mutableStateOf(" ") }
    var isEdited by remember{ mutableStateOf(items.isEditing) }
    Row (modifier= Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly){

        Column {
            BasicTextField(value = editedItem,
                onValueChange ={editedItem=it} ,
                singleLine = true,
                modifier= Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = editedItem)
            }

            BasicTextField(value = editedQty,
                onValueChange ={editedQty=it} ,
                singleLine = true,
                modifier= Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text =editedQty)
            }

            Button(onClick = {
                isEdited=false
                onEditComplete(editedItem,editedQty)

            }) {
                Text(text = "Save")
            }
        }
    }

}
