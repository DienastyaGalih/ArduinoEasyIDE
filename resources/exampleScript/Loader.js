/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//$(document).ready(function() {
//    $(".mm").click(function(event) {
//        alert(event.target.id);
//    });
//});
//
////$("div").click({param1: "Hello", param2: "World"}, cool_function);
//
//
//function cool_function(event) {
//    alert(event.data.param1);
//    alert(event.data.param2);
//}
//
//$("#other").click(function () {
//    $("#target").dblclick();
//});
//function person(name, isLoad) {
//    this.name = name;
//    this.isLoad = isLoad;
//}
//
//var tab=[];
//
//tab[0]=new person("file","isload");
var budi=function (){
    this.makan="minum";
    this.getMakan=function (){
        alert("yes");
        return this.makan;
    }
}

var budiObject=new budi();

function minum(){
    
    alert(budiObject.getMakan());
}
minum();
//minum();

//var myFather = new person("John", "Doe", 50, "blue");
//var myMother = new person("Sally", "Rally", 48, "green");
//
//myFather.age=10;
//var tabFileArray;
//var tabFile =  {
//    name: "",
//    status: ""
//};
//
//tabFileArray=new tabFile;


