// import { getCords } from './scriptcopy';

var frm = $('#fileUploader');
var modal = $("#myModal");
var pending =$("#pend");
var completed = $('#completed');
var errormsg = $('#error');
var errorsCount = 0;


frm.submit(function (e) {
  
  e.preventDefault();

  function validateForm() {
//    $('#fileUploader').val()==="";
//    if($('#jsonFileUpload').val()===""||$('#excelFileUpload').val()===""||$('#pdfFileUpload').val()==="" ){
//      return false;
//    }
    return true;
  }
  let isValid = validateForm();
  // потом поменять надо
  if(isValid){
    data = new FormData();
    data.append("excelFileUpload", $('#excelFileUpload')[0].files[0]);
    data.append("pdfFileUpload", $("#pdfFileUpload")[0].files[0]);
    data.append("jsonFileUploader", getCords());
    // data.append('jsonCoeds',getCords())
    console.log(getCords());

    $.ajax({
      type: frm.attr('method'),
      url: frm.attr('action'),
      data: data,
      enctype: 'multipart/form-data',
      processData: false,
      contentType: false,
      success: function (data) {
          showModal();
          progressCheckAjax();
          console.log('Uploading was successful.'+ data.length);
          console.log(data);
      },
      error: function (data) {
          console.log('An error occurred.');
          console.log(data);
      },
    });
  }
  else{
    alert('Please upload all files');
  }
});
var modal = document.getElementById("myModal");
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];

function showModal() {
  modal.style.display = "block";  
}

function progressCheckAjax(){
  function disableF5(e) { if ((e.which || e.keyCode) == 116) e.preventDefault(); };
  $(document).on("keydown", disableF5);
  completed.hide();   
  pending.show();
  let intervalID = setTimeout(async () => {   
    let url = 'http://localhost:3000/check_status';
    $.ajax({
      type: frm.attr('method'),
      url: url,
      data: data,
      processData: false,
      contentType: false,
      success: function (data) {
        console.log('ready or pending'+ data.length);
        console.log(data);
        if(data == 'ready'){
          pending.hide();   
          completed.show();   
          span.onclick = function() {
            modal.style.display = "none";
          }
        }else{
          span.onclick = function() {  
            let conf = confirm("Вы точно хотите покинуть окно? Процесс будет потерян");
            if(conf){
              modal.style.display = "none";
            }
          }
          if(modal.style.display != "block"){
            clearInterval(intervalID);
          }
          else{            
            progressCheckAjax();
          }                
        }
      },
      error: function (data) {
        console.log('An error occurred.');
        errorsCount++;
        if(errorsCount>3){
          pending.hide()
          errormsg.show();
        }
        else{
          progressCheckAjax();
        }               
      },      
    })
  }, 1000);
}
