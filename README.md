# WeatherForecast

## How to build and run the code.  

**Step 1 :**  
  Download or clone the code in the local machine.  

**Step 2 :**
  Unzip and Open the code in android studio.

**Step 3 : (Create Google maps API key)**

  Create your own google map key from google api console. https://console.developers.google.com/
 
  Create new project in google api console.
  
  Enable Maps SDK for Android	 and Places Api in the library.
  
  Go to credentials section and you can see your api key.
  
  Go ahead and restrict the api for android apps and provide your debug SHA-1 signing-certificate fingerprint
  and Project package name (com.senthil.prabhu.android.weatherforecast) and save.
  
  To get your SHA-1 signing-certificate fingerprint follow the instruction in the edit page. 
  
  Copy the API key and replace in #res->values->strings.xml 
  `<string name="google_maps_api_key">YOUR API KEY</string>`

**Step 4 : (In case if your want to change Openweather API key)**
  
  Head over to http://openweathermap.org
  
  Register an account on the website
  
  After registration, go to your personal page
  
  Copy the API key that is generated for you

  Copy the API key and replace in #res->values->strings.xml 
  `<string name="open_weather_api_key">YOUR API KEY</string>`

 **Step 5 :**
 
  Connect the device or open the emulator. If you are connecting the device make sure the device usb debuging mode is enabled.   
  
## How the coverage reports can be generated.

  Right click the project → Run Tests with Coverage. 
  This will output our code coverage metrics.
