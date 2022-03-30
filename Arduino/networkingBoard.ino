#include <SPI.h>
#include <MFRC522.h>
#include <SoftwareSerial.h>
#include <Wire.h>

constexpr uint8_t RST_PIN = 9;     // Configurable, see typical pin layout above
constexpr uint8_t SS_PIN = 10;     // Configurable, see typical pin layout above

MFRC522 rfid(SS_PIN, RST_PIN); // Instance of the class

MFRC522::MIFARE_Key key;
SoftwareSerial bt(2, 3);

// Init array that will store new NUID
byte nuidPICC[4];
boolean isOn = false;

void setup()
{
  Serial.begin(9600);
  bt.begin(9600); // 블루투스를 사용하기 위해 초기화
  SPI.begin(); // Init SPI bus
  rfid.PCD_Init(); // Init MFRC522
  Serial.println("ready");
  Wire.begin();

  for (byte i = 0; i < 6; i++)
    key.keyByte[i] = 0xFF;

  Serial.println(F("This code scan the MIFARE Classsic NUID."));
  Serial.print(F("Using the following key:"));
  printHex(key.keyByte, MFRC522::MF_KEY_SIZE);
}

void loop() {
  if (bt.available())
  {
    char rtn = bt.read();
    if (rtn == 'C')
    {
      Serial.write("Connected!");
      Serial.println();
      while (!userAuthentication());
      bt.println("OK");
      Wire.beginTransmission(8);
      Wire.write('Z');
      Wire.endTransmission();
    }
    else if (rtn == 'D')
    {
      Serial.write("Disconnected!");
      Serial.println();
      Wire.beginTransmission(8);
      Wire.write('X');
      Wire.endTransmission();
    }
  }
  
  // Look for new cards
  if ( ! rfid.PICC_IsNewCardPresent())
    return;

  // Verify if the NUID has been readed
  if ( ! rfid.PICC_ReadCardSerial())
    return;

  Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  Serial.println(rfid.PICC_GetTypeName(piccType));

  // Check is the PICC of Classic MIFARE type
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI &&
      piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
      piccType != MFRC522::PICC_TYPE_MIFARE_4K)
  {
    Serial.println(F("Payment zone"));
    bt.print("PAYMENT");
    bt.println();
    return;
  }

  if (rfid.uid.uidByte[0] != nuidPICC[0] ||
      rfid.uid.uidByte[1] != nuidPICC[1] ||
      rfid.uid.uidByte[2] != nuidPICC[2] ||
      rfid.uid.uidByte[3] != nuidPICC[3] )
  {
    // Store NUID into nuidPICC array
    for (byte i = 0; i < 4; i++)
      nuidPICC[i] = rfid.uid.uidByte[i];

    printHex(rfid.uid.uidByte, rfid.uid.size);
    printPhoneHex(rfid.uid.uidByte, rfid.uid.size);
    Serial.println();
    printDec(rfid.uid.uidByte, rfid.uid.size);
    Serial.println();
  }
  else
  {
    printHex(rfid.uid.uidByte, rfid.uid.size);
    printPhoneHex(rfid.uid.uidByte, rfid.uid.size);
  }

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
}

/**
   Helper routine to dump a byte array as hex values to Serial.
*/
void printHex(byte *buffer, byte bufferSize)
{
  for (byte i = 0; i < bufferSize; i++)
  {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}

/**
   Helper routine to dump a byte array as dec values to Serial.
*/
void printDec(byte *buffer, byte bufferSize)
{
  for (byte i = 0; i < bufferSize; i++)
  {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
  }
}

void printPhoneHex(byte *buffer, byte bufferSize)
{
  for (byte i = 0; i < bufferSize; i++)
  {
    bt.print(buffer[i] < 0x10 ? "0" : "");
    bt.print(buffer[i], HEX);
  }
  bt.println();
}

boolean userAuthentication()
{
  Wire.requestFrom(8, 1);
  char c = Wire.read();
  if (c == 'O') return true;
  else return false;
}
