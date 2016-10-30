/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var project;
var tabFileArrayID = [];
var tabActive = 0;
var countingTabArrayId = 0;




//var tabProjectActive = new tabProject();

$(document).ready(function () {
    console.log("document loaded");
    var jqxhr = $.get("http://localhost:8080/project/openProject/makan", function (data) {

        project = data;



        $("#projectName").text("~" + project.name);
        $("#arduinoType").text(project.config.arduinoType);
        $("#arduinoIC").text(project.config.icType);

        updateProjectStructure(project.sourceCode);



    }).done(function () {
        alert("second success ");
    }).fail(function (detail) {
        alert("error " + detail);
    }).always(function () {
        alert("finished");
    });



    $("#saveProject").click(function () {
        // Send the data using post

        alert("saving " + tabFileArrayID[0]);

        var $f = $('#codeSource_' + tabFileArrayID[0]);
        var data = $f.get(0).contentWindow.getValueEditor();
        alert(data + " data source");

//        $.post("http://localhost:8080/project/saveFile/make", {idFile: tabFileArrayID[0], sourceFile: "makan && minum"})
//                .done(function (data) {
//                    alert("Data Loaded: " + data.makan);
//                }).fail(function (data) {
//            alert("error " + data);
//        });



        $.ajax({
            url: 'http://localhost:8080/project/saveFile/'+tabFileArrayID[0],
            type: "POST",
            processData: false,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (message) {
                // Do something with response
                alert(message.makan);
            }
        });




//        var posting = $.post("http://localhost:8080/project/saveFile/", JSON.stringify({idFile: "galih", projectValue: tabFileArrayID[0]}), function () {
//            alert("succes " );
//        }, "json");
//
//        posting.done(function (data) {
//            alert("finish save " + data);
////            $("textarea#debug").val(data);
////            showDialog('dialog9');
////            $('textarea#debug').scrollTop($('textarea#debug')[0].scrollHeight);
//        });


//        for (var i = 0; i < tabFileArrayID.lenght; i++) {
//            var posting = $.post(url, {idFile: "galih", projectValue: tabFileArrayID[i]});
//
//
//            // Put the results in a div
//            posting.done(function (data) {
//                alert("finish save " + data);
////            $("textarea#debug").val(data);
////            showDialog('dialog9');
////            $('textarea#debug').scrollTop($('textarea#debug')[0].scrollHeight);
//            });
//        }

    });


    $("#backButtonPanel").click(function () {
        var charm = $("#menu-special").data("charm");

        charm.close();
    });

    $("#newProjectButton").click(function () {

        $(".sideBarLi").removeClass("active");
        $("#newProjectButton").addClass("active");

        $(".sidePanelMenuFile").hide( );
        $("#newProjectPanel").show( );
    });


    $("#openProjectButton").click(function () {
        $(".sideBarLi").removeClass("active");
        $("#openProjectButton").addClass("active");

        $(".sidePanelMenuFile").hide();
        $("#openProjectPanel").show( );
    });



    $("#virifyButton").click(function (event) {
        var $f = $("#frameCode");
        $f.get(0).contentWindow.setValueEditor("makan"); //works    
    });

});
$(window).load(function () {

});

function updateProjectStructure(project) {
    //render file project

    var filesText = "";
    var foldersText = "";
    alert("makan");
    for (var p = 0; p < project.folders.length; p++) {
        filesText += addFolder(project.folders[p]);
    }
    for (var i = 0; i < project.files.length; i++) {
        filesText += addFile(project.files[i]);
    }
    $("#projectBar").append(filesText);

    $(document).ready(function () {
        $(".fileNode").dblclick(function (event) {
            tabActive = event.target.id;
            alert(event.target.id);
            $("#dialog9").data('dialog').open();
            addTabFile(event.target.id)

        });
    });


}


function addTabFile(idFIle) {


    var tmp = idFIle.split('_', 3);
    idFIle = tmp[1];
    var fileName = tmp[2];

    tabFileArrayID[countingTabArrayId++] = idFIle;

//    var tabFileId = 'frame_' + idFIle.replace(".", "").replace("/", "");

    var tabFileId = "fileTab_" + idFIle;
    var sourceFileId = "fileSource_" + idFIle;
    var codeFileId = "codeSource_" + idFIle;
    $("#tabCode").append('<li class="tabPanel" id="' + tabFileId + '" ><a id="' + tabFileId + '" href="#' + sourceFileId + '">' + fileName + '</a><span id="' + tabFileId + '" style="margin-top:  -6px;margin-left: -17px; color: #eaeaea" class="on-left mif-cancel fg-hover-yellow "></span></li>');
    var sourcePane = '<div class="frame" id="' + sourceFileId + '" style="margin-top: 0px;padding: 0px">' +
            '<iframe id="' + codeFileId + '"  name="mmm"  frameborder="1" width="100%" height="555" src="editor/editor2.html"></iframe>' +
            '</div>';

    $("#tabSource").append(sourcePane);

    $("#dialog9").data('dialog').close();


    $(document).ready(function () {
        $(".tabPanel").click(function (event) {
            requestCodeSource(codeFileId);

        });
    });


}

function closeTab(idFile) {

}

/**
 * danger user static valu for checking
 * @param {type} codeFileId
 * @returns {undefined}
 */
function requestCodeSource(codeFileId) {

    var $f = $('#' + codeFileId);
    if ($f.get(0).contentWindow.getValueEditor() != 'null') {
        return;
    }
    var makeUI = $.get("http://localhost:8080/project/loadFile/namaFIle" + codeFileId, function (data) {
        var $f = $('#' + codeFileId);
        $f.get(0).contentWindow.setValueEditor(data);
    }).done(function () {
        alert("second success ");
    }).fail(function (data) {
        alert("error " + data);
    }).always(function () {
        alert("finished");
    });

}


function addFolder(folder) {
    //find folder
    var filesText = "";
    for (var p = 0; p < folder.files.length; p++) {
        filesText += addSubFile(folder.files[p], folder.name);
    }
    return '<li class="node" ><span class="leaf"><span class="mif-folder"></span> ' + folder.name + '</span><span class="node-toggle"></span><ul>' + filesText + '</ul></li>';
}


function addSubFile(file, folderName) {
    return '<li class="fileNode" id="fileBar_' + file.id + '_' + file.name + '" ><span id="fileBar_' + file.id + '_' + file.name + '" class="leaf"><span id="fileBar_' + file.id + '_' + file.name + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
}

function addFile(file) {
    return '<li class="fileNode" id="fileBar_' + file.id + '_' + file.name + '" ><span id="fileBar_' + file.id + '_' + file.name + '" class="leaf"><span id="fileBar_' + file.id + '_' + file.name + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
}


function specialClick() {
    var charm = $("#menu-special").data("charm");
    if (charm.element.data("opened") === true) {
        charm.close();
    } else {
        charm.open();
    }
}

function getFileSourceFromEditor() {




}

