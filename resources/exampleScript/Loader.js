/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $(".mm").click(function(event) {
        alert(event.target.id);
    });
});

//$("div").click({param1: "Hello", param2: "World"}, cool_function);


function cool_function(event) {
    alert(event.data.param1);
    alert(event.data.param2);
}

$("#other").click(function () {
    $("#target").dblclick();
});