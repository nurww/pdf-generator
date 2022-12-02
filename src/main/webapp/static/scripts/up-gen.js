// validation of uploaded files format and saving to local storage 
let validationForm = document.getElementById('fileUploader');

let excelUploader = document.getElementById('excelFileUpload');
let pdfUploader = document.getElementById('pdfFileUpload');
let useMadeJson = document.getElementById('makeJson');
let needNewJson = document.getElementById('needJson');
let jsonUploader = document.getElementById('jsonFileUpload');
let jsonLabel = document.getElementById('labeljson');

excelUploader.addEventListener("change",ValidateExcel);
pdfUploader.addEventListener("change",ValidatePdf);
jsonUploader.addEventListener("change",ValidateJson);
useMadeJson.addEventListener("change",switcher);
needNewJson.addEventListener("change",displayJsonInput);

function switcher(){
    jsonLabel.hidden = true;
    jsonUploader.hidden = true;
    jsonUploader.value = "";
}

function displayJsonInput(){
    jsonLabel.hidden = false;
    jsonUploader.hidden = false;    
}

function ValidateExcel() {
    let myFile = this.files[0];
    let isExcel = myFile.name.match('xlsx*');   
    console.log(myFile);
    if(isExcel) {
        let format = "excel";
        saveFile(myFile,format);
    }
    else{
        alert('Please upload excel file in .xlsx format');;
        excelUploader.value = "";
        return;
    }
}

function ValidatePdf() {
    let myFile = this.files[0];
    let isPdf = myFile.type.match('pdf*');
    if(isPdf) {
        let format = "pdf";
        saveFile(myFile,format);        
    }
    else{     
        alert('Please upload excel file in .pdf format');;
        pdfUploader.value = "";
        return;
    }
}

function ValidateJson() {
    let myFile = this.files[0];
    let isJson = myFile.type.match('json*');
    if(isJson) {
        let format = "json";
        saveFile(myFile,format);        
    }
    else{     
        alert('Please upload excel file in .json format');;
        jsonUploader.value = "";
        return;
    }
}

function saveFile(file,format) {
    if(format =="pdf") {
        const reader  = new FileReader();
        reader.addEventListener('load',() => {
            sessionStorage.setItem('pdf-recent',reader.result);
        })
        reader.readAsDataURL(file);
    } if(format =="excel") {
        const reader  = new FileReader();
        reader.addEventListener('load',() => {
            sessionStorage.setItem('excel-recent',reader.result);
        })
        reader.readAsDataURL(file);
    } if(format =="json") {
        const reader  = new FileReader();
        reader.addEventListener('load',() => {
            sessionStorage.setItem('json-recent',reader.result);
        })
        reader.readAsDataURL(file);
    } else {
        console.log(file,format);
    }
    
}