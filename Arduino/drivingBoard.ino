#include <SoftwareSerial.h>
#include <Wire.h>

#define OK 200
#define TOO_SHORT 100
#define TOO_LONG 300
int connectedFlag = 0
;
int d1 = 50, d2 = 50, d3 = 50;
int m1In1 = 7, m1In2 = 6, m2In1 = 4, m2In2 = 5; // 앞
int m3In1 = 12, m3In2 = 13, m4In1 = 11, m4In2 = 10; // 뒤
int s1Echo = 9, s1Trig = 8, s2Echo = 17, s2Trig = 16, s3Echo = 15, s3Trig = 14;

void setup() 
{
  // put your setup code here, to run once:
  _init();
}

void loop() 
{
  // put your main code here, to run repeatedly:
  
// ultrasonic wave
  d1 = ultrasonic(s1Echo, s1Trig);
  d2 = ultrasonic(s2Echo, s2Trig);
  d3 = ultrasonic(s3Echo, s3Trig);
  Serial.print(d1);
  Serial.print(" ");
  Serial.print(d2);
  Serial.print(" ");
  Serial.print(d3);
  Serial.println(" ");

//  controlling moter
  if (connectedFlag == 1)
  {
    if (isInRange(d2) == TOO_SHORT)
    { // 너무 가까우면 뒤로가기
      digitalWrite(m1In1, LOW);
      digitalWrite(m1In2, HIGH);
      digitalWrite(m2In1, LOW);
      digitalWrite(m2In2, HIGH);
      digitalWrite(m3In1, LOW);
      digitalWrite(m3In2, HIGH);
      digitalWrite(m4In1, LOW);
      digitalWrite(m4In2, HIGH);
    }
    else if (isInRange(d2) == OK)
    { // 적당한 거리인 경우 멈춤
      digitalWrite(m1In1, LOW);
      digitalWrite(m1In2, LOW);
      digitalWrite(m2In1, LOW);
      digitalWrite(m2In2, LOW);
      digitalWrite(m3In1, LOW);
      digitalWrite(m3In2, LOW);
      digitalWrite(m4In1, LOW);
      digitalWrite(m4In2, LOW);
    }
    else
    {
      if (isInRange(d1) == TOO_LONG && isInRange(d3) == TOO_LONG) // 전진
      {
        digitalWrite(m1In1, HIGH);
        digitalWrite(m1In2, LOW);
        digitalWrite(m2In1, HIGH);
        digitalWrite(m2In2, LOW);
        digitalWrite(m3In1, HIGH);
        digitalWrite(m3In2, LOW);
        digitalWrite(m4In1, HIGH);
        digitalWrite(m4In2, LOW);
      }
      else if (isInRange(d1) <= OK && isInRange(d3) == TOO_LONG) // 좌회전
      {
        digitalWrite(m1In1, HIGH);
        digitalWrite(m1In2, LOW);
        digitalWrite(m2In1, LOW);
        digitalWrite(m2In2, HIGH);
        digitalWrite(m3In1, HIGH);
        digitalWrite(m3In2, LOW);
        digitalWrite(m4In1, LOW);
        digitalWrite(m4In2, HIGH);
      }
      else if (isInRange(d3) <= OK && isInRange(d1) == TOO_LONG) // 우회전
      {
        digitalWrite(m1In1, LOW);
        digitalWrite(m1In2, HIGH);
        digitalWrite(m2In1, HIGH);
        digitalWrite(m2In2, LOW);
        digitalWrite(m3In1, LOW);
        digitalWrite(m3In2, HIGH);
        digitalWrite(m4In1, HIGH);
        digitalWrite(m4In2, LOW);
      }
    }
  }
  else // connecting 해제 시 모터 중지
  {
    digitalWrite(m1In1, LOW);
    digitalWrite(m1In2, LOW);
    digitalWrite(m2In1, LOW);
    digitalWrite(m2In2, LOW);
    digitalWrite(m3In1, LOW);
    digitalWrite(m3In2, LOW);
    digitalWrite(m4In1, LOW);
    digitalWrite(m4In2, LOW);
  }
}

int ultrasonic(int echo, int trig) 
{
  int duration, distance;

  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);

  duration = pulseIn(echo, HIGH);
  distance = duration / 58;
  
  return distance;
}

int isInRange(int d) 
{
  if (d >= 30) return TOO_LONG;
  else if (d >= 15 && d < 30) return OK;
  else if (d >= 0) return TOO_SHORT;
}

void receiveConnData(int size) 
{
  while (0 < Wire.available()) 
  {
    char c = Wire.read();
    if (connectedFlag == 0 && c == 'Z')
    {
      Serial.print("starting cart");
      Serial.println();
      connectedFlag = 1;
    }
    else if (connectedFlag == 1 && c == 'X')
    {
      Serial.println("stopping cart");
      connectedFlag = 0;
    }
  }
}

void requestConnData() 
{
  (isInRange(d2) <= OK) ? Wire.write('O') : Wire.write('X');
}
void _init() 
{
  Serial.begin(9600); // 시리얼 통신 초기화
  Serial.println("Ready i^2c");
  
  // DC moter
  pinMode(m1In1, OUTPUT);
  pinMode(m1In2, OUTPUT);
  pinMode(m2In1, OUTPUT);
  pinMode(m2In2, OUTPUT);
  pinMode(m3In1, OUTPUT);
  pinMode(m3In2, OUTPUT);
  pinMode(m4In1, OUTPUT);
  pinMode(m4In2, OUTPUT);
  
  // Wire
  Wire.begin(8);
  Wire.onReceive(receiveConnData);
  Wire.onRequest(requestConnData);

  // ultrasonic wave
  pinMode(s1Echo, INPUT);
  pinMode(s1Trig, OUTPUT);
  pinMode(s2Echo, INPUT);
  pinMode(s2Trig, OUTPUT);
  pinMode(s3Echo, INPUT);
  pinMode(s3Trig, OUTPUT);
}
