console.log("Starting Poste Device v10:37");

//AWS
var AWS = require('aws-sdk'); 
AWS.config.update({region: 'us-east-1'});
var kinesis = new AWS.Kinesis();

//Grove
var grove = require('jsupm_grove');
var light = new grove.GroveLight(1);
var led = new grove.GroveLed(2);
led.off();


function poll(){
  var value = light.value();
  putRecord(value);
  if(value < 20){
    led.on();
  }else {
    led.off();
  }
}

function putRecord(value){
    var currTime = new Date().getMilliseconds();

    var record = JSON.stringify({
      time : currTime,
      reading : value
    });

    var recordParams = {
      Data : ""+value, //record,
      PartitionKey : "temperature-sensor-01",
      StreamName : "poste-stream"
    };

    kinesis.putRecord(recordParams, function(err, data) {
      if (err) {
        console.error(err);
      }
      else {
        console.info('Record put: '+value);
      }
    });
}

var poll_freq = 1000;

setInterval(poll,poll_freq);
