console.log("Starting Loudwatch v16:54");
//sensors setup
var loudnessSensor = require('jsupm_groveloudness');
var myLoudnessSensor = new loudnessSensor.GroveLoudness(0);

var groveSensor = require('jsupm_grove');
var led = new groveSensor.GroveLed(2);
led.off();

//aws setup
var AWS = require('aws-sdk'); 
AWS.config.update({region: 'us-east-1'});
var cloudwatch = new AWS.CloudWatch();

var signal = [];

var too_loud = 300;

function listen(){
  var loudness = myLoudnessSensor.value();  
  signal.push(loudness);
  //console.log(loudness);
}

function chew(){  
  var clone = signal.slice(0);
  var sum = 0;
  var min = 999;
  var max = -1;
  
  signal = [];
  for(var i=0; i<clone.length;i++){
    var curr = clone[i];
    if (curr < min) min = curr;
    if (curr > max) max = curr;
    sum += curr;
  }
  var samples = clone.length;
  if (max > too_loud){
    led.on();
  }else{
    led.off();
  }    
  ship(min,max,samples,sum); 
}

function ship(min,max,samples,sum){
    console.log("[metric-datum:(min="+min+",max="+max+",samples="+samples+",sum="+sum+")]");
    var params = {
      MetricData: [
        {
          MetricName: 'Loudness',
          StatisticValues: {
            Maximum: max,
            Minimum: min,
            SampleCount: samples,
            Sum: sum,
          },
          Timestamp: new Date(),
          Unit: 'None',
        }
      ],
      Namespace: 'IoT' /* required */
    };
    cloudwatch.putMetricData(params, function(err, data) {
      if (err) console.log(err, err.stack); // an error occurred
      else     console.log(data);           // successful response
    });
}

var sample_freq = 25;
var push_freq=5000;
setInterval(listen,sample_freq);
setInterval(chew,push_freq);