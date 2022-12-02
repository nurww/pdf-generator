const cords = [];

(function(arrOfCords) {
    var bodyObj = {};
            
    let canv = document.getElementById('the-canvas');
    ctx = canv.getContext('2d');
    
    const objOfCords = arrOfCords.reduce((acc,cord) => {
        acc[cord._id] = cord;
        return acc;
    },{});

    // UI interactive Elements
    const downloadButton = document.querySelector('#downloadJson');
    const workspace = document.querySelector('#test');

    // event listeners
    workspace.addEventListener('dblclick',clickingWorkspacehandler );
    downloadButton.addEventListener('click',downloadFunction);

    // Events

    class InputHolder {
        constructor(x,y){
            this.x = x;
            this.y = y;
            this.id = `${Math.floor(Math.random()*100000)}`;
            this.inpText = '';
            this.canva = document.createElement('canvas');
            this.ctx = this.canva.getContext('2d');
            this.coursor = new Coursor(this.x,this.y,this.id);
            this.editable = true;
            this.fontSize = 15;
            this.fontFamily = "Arial";
            this.parent = document.getElementById('the-canvas');
            this.onKeyPress = function(e) {
                if(this.editable == false) {
                    return;
                }
                if(e.key =='Backspace') {
                    this.inpText = this.inpText.slice(0, -1);                 
                }
                else if (e.key == 'Enter') {
                    if(this.inpText ==""|| this.inpText ==" "){                   
                        this.editable = false;
                        this.delete();
                    }
                    else{
                        this.editable = false;
                        this.coursor.editable = false;
                        this.save();
                    }
                }
                else {                    
                    if (e.which >=48 && e.which<=57 || e.which>=65 && e.which<=90 || e.which >=96 && e.which <=105 || e.which ==32) {
                        this.inpText += `${e.key}`;
                    }
                }
                this.render();
            }.bind(this);

            this.onFocusCheck = function(e) {
                if(this.inpText ==""|| this.inpText ==" "){                  
                    // console.log(parent,this.canva);
                    this.editable = false;
                    this.delete();
                }
                else{
                    this.editable = false;
                    this.coursor.editable = false;
                    this.save();
                }
            }.bind(this);

            this.stopProp = function(e) {
                e.stopPropagation();
            }.bind(this);

            this.onDelete = function(e) {
                e.preventDefault();
                e.stopPropagation();
                if(!this.editable){
                    console.log(this.inpText + 'wasnt editable,deleting') ;

                    this.delete();     
                }
                else{
                    this.editable = false;
                    this.coursor.editable = false;
                    console.log(this.inpText + 'was editable,deleting') ;

                    this.delete();                         

                }                       
            }.bind(this);

            this.onEdit = function(e) {
                e.stopPropagation();
                this.editable = true;
                this.coursor.editable = true;
                this.render()
                this.save();
            }.bind(this);
        }
        
        create() {           
            let id = this.id;
            this.canva.setAttribute('id','input_'+id);
            this.canva.setAttribute('tab-index','0');
            this.canva.focus();
            this.canva.setAttribute('data-editable','true');
            this.canva.width = 70;
            this.canva.height = Number(this.fontSize)+2;
            this.canva.style.position = "absolute";
            this.ctx.font = this.fontSize +"px "+this.fontFamily;
            this.ctx.fillStyle = 'white';
            this.ctx.rect(0, 0, 70, this.canva.height);
            this.ctx.fillRect(0,0,70, this.canva.height);
            this.ctx.fillStyle = 'black';
            let container = document.querySelector('#test');
            container.appendChild(this.canva);
            this.coursor.create(this.fontSize);
            this.coursor.editable = true;
            document.addEventListener("keyup", this.onKeyPress);            
            this.canva.addEventListener('contextmenu',this.onDelete);       
            this.canva.addEventListener('click',this.onEdit);
            this.canva.addEventListener('dblclick',this.stopProp);          
            this.parent.addEventListener('dblclick',this.onFocusCheck);
            this.render();
        }
        save(){
            sendCords(this.x,this.y);
            const newCord = {
                _id:this.id,
                title:this.inpText,
                body:bodyObj
            };
            objOfCords[newCord._id]= newCord ;   
            return {...newCord};
        }
      
        render(){
            this.canva.style.left = this.x+"px";
            this.canva.style.top = this.y- Number(this.fontSize)+"px";
            this.ctx.fillStyle = 'white';
            this.ctx.fillRect(0,0,this.canva.width, this.canva.height);
            this.ctx.fillStyle = 'black';
            let width = Math.ceil(this.ctx.measureText(this.inpText).width);
            if(width > this.canva.width){
                this.canva.width = width;  
                this.ctx.canvas.width = width;
                this.ctx.font = this.fontSize +"px "+this.fontFamily;
                this.ctx.rect(0, 0, this.canva.width, this.canva.height);
                this.ctx.fillStyle = 'white';
                this.ctx.fillRect(0, 0, this.canva.width, this.canva.height);
                this.ctx.fillStyle = 'black';
            }
            else if(width != this.canva.width && 70 < this.canva.width){
                this.canva.width = width;  
                this.ctx.canvas.width = width;
                this.ctx.font = this.fontSize +"px "+this.fontFamily;
                this.ctx.rect(0, 0, this.canva.width, this.canva.height);
                this.ctx.fillStyle = 'white';
                this.ctx.fillRect(0, 0, this.canva.width, this.canva.height);
                this.ctx.fillStyle = 'black';
            }            
            this.ctx.fillText(`${this.inpText}`,0,Number(this.fontSize),width);
            this.coursor.x = this.x + width;
        }

        delete() {
            delete(objOfCords[this.id]);
            this.canva.remove();
            let cour = document.querySelector('#coursor_'+this.id);
            cour.remove();
            console.log("deleted ")
        }
    }
    
    class Coursor{
        constructor(x,y,id){      
            this.editable = false;
            this.x = x;
            this.y = y;
            this.id = id;
            this.display = true;
            this.coursor = null;
            this.size = 14;
        }
        create(size){
            this.size = size;
            let id = this.id;
            this.editable = true;
            let container = document.querySelector('#test');
            this.coursor = document.createElement('canvas');
            this.coursor.setAttribute('id','coursor_'+id);
            let ctx = this.coursor.getContext('2d');
            this.coursor.width = 2;
            this.coursor.height = size;
            this.coursor.style.position = "absolute";
            ctx.fillStyle = 'black';
            ctx.rect(0, 0, 2, size);
            ctx.fillRect(0,0,2,size);
            container.appendChild(this.coursor);
            let fn =this.render.bind(this);
            setInterval(fn, 500);
        }
        render(){
            this.display = !this.display;
            this.coursor.style.left = this.x+"px";
            this.coursor.style.top = this.y-this.size+2+"px";
            this.coursor.style.position = "absolute";
            if(this.editable && this.display){
                this.coursor.style.display = 'block';
            }else{
                this.coursor.style.display = 'none';
            }
        }
    }   

    function clickingWorkspacehandler(event){
        event = event || window.event;
        var x = event.offsetX;
        var y = event.offsetY;      
        displayOnCanva(x,y);
    }

    function displayOnCanva(x,y){
        let fsInput = document.getElementById("font-scroller");
        let ffInput = document.getElementById("font-family");
        let fontsizee = fsInput.value;
        let fontfamily = ffInput.value;
        let inputHolder = new InputHolder(x , y);
        inputHolder.fontSize = fontsizee;
        inputHolder.fontFamily = fontfamily;
        inputHolder.create();
    }
      
    function sendCords(x,y){
        bodyObj = {
            x:x,
            y:y,
        }
        return bodyObj;
    }  
    
    function downloadFunction(){
        var dataStr = "data:text/json;charset=utf-8,"+ encodeURIComponent(JSON.stringify(objOfCords));
        var dlAnchorElem = document.getElementById('downloadAnchorElem');
        dlAnchorElem.setAttribute("href",dataStr);
        dlAnchorElem.setAttribute("download", "coordinates.json");
        dlAnchorElem.click();
    }
})(cords);
