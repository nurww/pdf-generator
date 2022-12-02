const path = require("path")

const express = require('express')
const fileUpload = require("express-fileupload")
const app = express()
const port = 3000

let counter = 0

app.use('/static', express.static('static'))
app.use(fileUpload());

app.get('/', (req, res) => {
  console.log(__dirname+" dir")
  res.sendFile( __dirname + "\\index.html")
})

app.post('/upload', (req,res) => {
  console.log(req.POST)
  console.log(req.files)
  res.send("ok")
})

app.post('/check_status', (req, res) => {
  console.log(req.POST);
  counter += 1;
  console.log(counter)
  if (counter % 5 == 0) {
    res.send('ready')
  } else {
    res.send('pending')
  }
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})