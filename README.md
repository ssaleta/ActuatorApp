#Actuator App
Android and Arduino project to to simulate control the actuator. I had tested this app on HC-06 module and Arduino Uno. 

#Foundation program:
- Android 4.3 +
- Paired android device with bluetooth module by NFC
- Paired only with one bluetooth module
- Send list byte[] with button status
- Bargraf with % status of actuator possition

#Librarys and packages:
- Butterknife 
- BluetoothSPP
- RoundCornerProgressBar

#Arduino Connection:
- I used Fritzing application to make a draw. I have to used HC-05 module on draw but it doesn't matter if you use module HC-05 or HC-06. Only difference is that HC-05 can act as a slave and master whereas HC-06 works only as a slave. 
![ScreenShot](https://cloud.githubusercontent.com/assets/12597823/20239854/d7f93aa8-a90a-11e6-9742-56858967ce96.png)




