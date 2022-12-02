<%-- 
    Document   : index
    Created on : 29 нояб. 2022 г., 13:16:42
    Author     : toktarkhan_n
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="//mozilla.github.io/pdf.js/build/pdf.js"></script>
        <title>PDF Selector - Excel parser</title>
        <link rel="stylesheet" href="<c:url value="/static/style.css"/>">
        <link rel="stylesheet" href="<c:url value="/static/modalstyle.css"/>">
        <link rel="icon" type="image/svg+xml" href="<c:url value="/static/logopdf.svg"/>" sizes="50x50" >
    </head>
    <body >
        <div class="header">
            <a href="#default" class="logo">{PDF} Generator</a>
            <div class="header-right">
                <a class="active" href="#home">Workspace</a>
                <a href="#about">About</a>
            </div>
        </div>

        <div class="container" >
            <div class="tooler">
                <div class="_tooler_topbar">
                    <form id="fileUploader" method='post' action='generate' target="empty" enctype="multipart/form-data">
                        <b> Upload Your files </b><br>
                        <label for="excel">Excel:</label>
                        <input type="file" name="excel" id="excelFileUpload" > <br><br>

                        <label for="pdf">PDF:</label>
                        <input type="file" name="pdf" id="pdfFileUpload"><br><br>

                        <input type="radio" name="jsonChoose" id="makeJson"><label for="makeJson">Use new</label><br><br>
                        <input type="radio" name="jsonChoose" id="needJson" checked><label for="needJson">Already have Json file?</label><br><br>

                        <label id="labeljson" for="json" hidden>Json:</label>
                        <input type="file" name="json" id="jsonFileUpload" hidden>

                        <input type="submit" class="button-47 " value="Generate"></input>
                    </form>
                </div>
                <hr>
                <div class="_tooler_downbar " >     

                    <div>
                        <b> Font Size </b>
                        <input id="font-scroller" type="text"  value="12"><br><br>
                        <b> Font Family </b><br>
                        <select name="font-family" id="font-family" style="width: 100%;">
                            <option value="Arial" selected >Arial</option>
                            <option value="Verdana">Verdana</option>
                            <option value="Tahoma">Tahoma</option>
                            <option value="Times New Roman">Times New Roman</option>
                            <option value="Georgia">Georgia</option>
                            <option value="Courier New">Courier New</option>
                            <option value="Trebuchet MS">Trebuchet MS</option> 
                            <option value="Brush Script MT">Brush Script MT</option>
                        </select>
                        <br>
                    </div>
                </div>
                <hr>
                <div class="_tooler_download">
                    <button type="submit" id= 'downloadJson' class="button-47 ">Download Json</button>
                    <a href="" id="downloadAnchorElem"></a>
                    <!-- <hr> -->

                    <!-- <form id="statusbar" method='post' action='http://localhost:3000/check_status' target="empty" encType="multipart/form-data">
                        <button id="status" type="submit" class="button-47 " value="statusCheck">Status Check</button>
                    </form> -->

                </div>     

            </div>
            <!-- <iframe name="empty" src=""></iframe> -->
            <div class="workspace" >
                <div class="wrap">
                    <div id="test" style="position: relative;">
                        <p> 
                            <span id="offx"></span>
                            <span id="offy"></span>
                        </p>
                        <canvas id="the-canvas" style="border: 1px solid black; z-index: 0;">
                        </canvas>
                    </div>
                    <div class="pagination">
                        <button id="prev" class="button-47 ">Previous</button>
                        &nbsp; &nbsp;
                        <span>Page: <b><span id="page_num"></span> / <span id="page_count"></span></b></span>
                        &nbsp; &nbsp;
                        <button id="next" class="button-47 ">Next </button>
                    </div>
                </div>            
            </div>

            <!-- workspace with PDF pages and canva -->
        </div>

        <!-- The Modal -->
        <div id="myModal" class="modal">

            <!-- Modal content -->
            <div class="modal-content">
                <div class="modal-header">
                    <h2>Generating</h2>
                </div>
                <div class="modal-body">
                    <div id="pend" hidden>
                        <p style="color:white ;font-weight:500">Pending...</p>
                        <div id="pendCircle" class="dot-spin " style=" margin: 30px auto;"></div>
                    </div>
                    <div id="completed" hidden>
                        <p id="completed" style="color:rgb(10, 170, 10) ;" >Completed!</p>

                        <button type="submit" id= 'downloadArchive' class="button-47 ">Download Archive</button>
                        <a href="" id="downloadArchivator"></a>
                    </div>
                    <div id="error" hidden>
                        <p id="errormsg" style="color:rgb(224, 16, 16) ;" > 404 - Error from server</p>
                    </div>
                </div>

                <span class="close">Close</span>

            </div>


        </div>


    </body>
</html>
<!--<script src="/static/scripts/up-gen.js"></script>
<script src="/static/scripts/scriptcopy.js"></script>
<script src="/static/scripts/pdfscript.js"></script>
<script src="/static/scripts/fileUpload.js"></script>
<script src="/static/scripts/checkStatus.js"></script>-->

<script src="<c:url value="/static/scripts/up-gen.js"/>"></script>
<script src="<c:url value="/static/scripts/mainScript.js"/>"></script>
<script src="<c:url value="/static/scripts/pdfscript.js"/>"></script>
<script src="<c:url value="/static/scripts/fileUpload.js"/>"></script>
<script src="<c:url value="/static/scripts/checkStatus.js"/>"></script>



