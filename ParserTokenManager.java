/* ParserTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. ParserTokenManager.java */
import net.ddp2p.ASN1.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Socket;

/** Token Manager. */
@SuppressWarnings("unused")public class ParserTokenManager implements ParserConstants {

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x7feL) != 0L)
         {
            jjmatchedKind = 25;
            return 36;
         }
         return -1;
      case 1:
         if ((active0 & 0x7feL) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 1;
            return 36;
         }
         return -1;
      case 2:
         if ((active0 & 0x7feL) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 2;
            return 36;
         }
         return -1;
      case 3:
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         if ((active0 & 0x118L) != 0L)
            return 36;
         if ((active0 & 0x626L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 3;
            return 36;
         }
         return -1;
      case 4:
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         if ((active0 & 0x204L) != 0L)
            return 36;
         if ((active0 & 0x422L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 4;
            return 36;
         }
         return -1;
      case 5:
         if ((active0 & 0x422L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 5;
            return 36;
         }
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      case 6:
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         if ((active0 & 0x22L) != 0L)
            return 36;
         if ((active0 & 0x400L) != 0L)
         {
            if (jjmatchedPos != 6)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 6;
            }
            return 36;
         }
         return -1;
      case 7:
         if ((active0 & 0x400L) != 0L)
            return 36;
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      case 8:
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      case 9:
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      case 10:
         if ((active0 & 0xc0L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      case 11:
         if ((active0 & 0x40L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 26;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 58:
         return jjStopAtPos(0, 23);
      case 59:
         return jjStopAtPos(0, 22);
      case 69:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 71:
         return jjMoveStringLiteralDfa1_0(0xc0L);
      case 76:
         return jjMoveStringLiteralDfa1_0(0x200L);
      case 80:
         return jjMoveStringLiteralDfa1_0(0x22L);
      case 82:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 84:
         return jjMoveStringLiteralDfa1_0(0xcL);
      case 85:
         return jjMoveStringLiteralDfa1_0(0x10L);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 65:
         return jjMoveStringLiteralDfa2_0(active0, 0xcL);
      case 69:
         return jjMoveStringLiteralDfa2_0(active0, 0x6c0L);
      case 82:
         return jjMoveStringLiteralDfa2_0(active0, 0x22L);
      case 83:
         return jjMoveStringLiteralDfa2_0(active0, 0x10L);
      case 88:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 65:
         return jjMoveStringLiteralDfa3_0(active0, 0x200L);
      case 69:
         return jjMoveStringLiteralDfa3_0(active0, 0x10L);
      case 71:
         return jjMoveStringLiteralDfa3_0(active0, 0x400L);
      case 73:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 75:
         return jjMoveStringLiteralDfa3_0(active0, 0x8L);
      case 79:
         return jjMoveStringLiteralDfa3_0(active0, 0x22L);
      case 83:
         return jjMoveStringLiteralDfa3_0(active0, 0x4L);
      case 84:
         return jjMoveStringLiteralDfa3_0(active0, 0xc0L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 69:
         if ((active0 & 0x8L) != 0L)
            return jjStartNfaWithStates_0(3, 3, 36);
         break;
      case 73:
         return jjMoveStringLiteralDfa4_0(active0, 0x400L);
      case 74:
         return jjMoveStringLiteralDfa4_0(active0, 0x22L);
      case 75:
         return jjMoveStringLiteralDfa4_0(active0, 0x4L);
      case 82:
         if ((active0 & 0x10L) != 0L)
            return jjStartNfaWithStates_0(3, 4, 36);
         break;
      case 84:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(3, 8, 36);
         break;
      case 86:
         return jjMoveStringLiteralDfa4_0(active0, 0x200L);
      case 95:
         return jjMoveStringLiteralDfa4_0(active0, 0xc0L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 69:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(4, 9, 36);
         return jjMoveStringLiteralDfa5_0(active0, 0x22L);
      case 80:
         return jjMoveStringLiteralDfa5_0(active0, 0xc0L);
      case 83:
         if ((active0 & 0x4L) != 0L)
            return jjStartNfaWithStates_0(4, 2, 36);
         return jjMoveStringLiteralDfa5_0(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 67:
         return jjMoveStringLiteralDfa6_0(active0, 0x22L);
      case 82:
         return jjMoveStringLiteralDfa6_0(active0, 0xc0L);
      case 84:
         return jjMoveStringLiteralDfa6_0(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjMoveStringLiteralDfa6_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 69:
         return jjMoveStringLiteralDfa7_0(active0, 0x400L);
      case 79:
         return jjMoveStringLiteralDfa7_0(active0, 0xc0L);
      case 84:
         if ((active0 & 0x20L) != 0L)
         {
            jjmatchedKind = 5;
            jjmatchedPos = 6;
         }
         return jjMoveStringLiteralDfa7_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private int jjMoveStringLiteralDfa7_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 74:
         return jjMoveStringLiteralDfa8_0(active0, 0xc0L);
      case 82:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(7, 10, 36);
         break;
      case 95:
         return jjMoveStringLiteralDfa8_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private int jjMoveStringLiteralDfa8_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(6, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 68:
         return jjMoveStringLiteralDfa9_0(active0, 0x2L);
      case 69:
         return jjMoveStringLiteralDfa9_0(active0, 0xc0L);
      default :
         break;
   }
   return jjStartNfa_0(7, active0);
}
private int jjMoveStringLiteralDfa9_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(7, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 67:
         return jjMoveStringLiteralDfa10_0(active0, 0xc0L);
      case 69:
         return jjMoveStringLiteralDfa10_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(8, active0);
}
private int jjMoveStringLiteralDfa10_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(8, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
   }
   switch(curChar)
   {
      case 70:
         return jjMoveStringLiteralDfa11_0(active0, 0x2L);
      case 84:
         if ((active0 & 0x80L) != 0L)
         {
            jjmatchedKind = 7;
            jjmatchedPos = 10;
         }
         return jjMoveStringLiteralDfa11_0(active0, 0x40L);
      default :
         break;
   }
   return jjStartNfa_0(9, active0);
}
private int jjMoveStringLiteralDfa11_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(9, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(10, active0);
      return 11;
   }
   switch(curChar)
   {
      case 73:
         return jjMoveStringLiteralDfa12_0(active0, 0x2L);
      case 83:
         if ((active0 & 0x40L) != 0L)
            return jjStopAtPos(11, 6);
         break;
      default :
         break;
   }
   return jjStartNfa_0(10, active0);
}
private int jjMoveStringLiteralDfa12_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(10, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(11, active0);
      return 12;
   }
   switch(curChar)
   {
      case 78:
         return jjMoveStringLiteralDfa13_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(11, active0);
}
private int jjMoveStringLiteralDfa13_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(11, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(12, active0);
      return 13;
   }
   switch(curChar)
   {
      case 73:
         return jjMoveStringLiteralDfa14_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(12, active0);
}
private int jjMoveStringLiteralDfa14_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(12, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(13, active0);
      return 14;
   }
   switch(curChar)
   {
      case 84:
         return jjMoveStringLiteralDfa15_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(13, active0);
}
private int jjMoveStringLiteralDfa15_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(13, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(14, active0);
      return 15;
   }
   switch(curChar)
   {
      case 73:
         return jjMoveStringLiteralDfa16_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(14, active0);
}
private int jjMoveStringLiteralDfa16_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(14, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(15, active0);
      return 16;
   }
   switch(curChar)
   {
      case 79:
         return jjMoveStringLiteralDfa17_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(15, active0);
}
private int jjMoveStringLiteralDfa17_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(15, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(16, active0);
      return 17;
   }
   switch(curChar)
   {
      case 78:
         if ((active0 & 0x2L) != 0L)
            return jjStopAtPos(17, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_0(16, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 37;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 24)
                        kind = 24;
                     jjstateSet[jjnewStateCnt++] = 4;
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 21)
                        kind = 21;
                  }
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 21)
                     kind = 21;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 3:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 4:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 6:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 7:
                  if (curChar == 45)
                     { jjAddStates(0, 1); }
                  break;
               case 8:
                  if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 9:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAdd(10); }
                  break;
               case 10:
                  if (curChar == 45)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 11:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 12:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 13:
                  if (curChar == 58)
                     { jjAddStates(2, 3); }
                  break;
               case 14:
                  if ((0x3f000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAdd(16); }
                  break;
               case 17:
                  if ((0x3f000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 18:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAdd(19); }
                  break;
               case 20:
                  if ((0x3f000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 21:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAdd(22); }
                  break;
               case 23:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 24;
                  break;
               case 24:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 25;
                  break;
               case 25:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 26;
                  break;
               case 27:
                  if (curChar == 48)
                     { jjCheckNAdd(22); }
                  break;
               case 28:
                  if (curChar == 54)
                     jjstateSet[jjnewStateCnt++] = 27;
                  break;
               case 29:
                  if (curChar == 48)
                     { jjCheckNAdd(19); }
                  break;
               case 30:
                  if (curChar == 54)
                     jjstateSet[jjnewStateCnt++] = 29;
                  break;
               case 31:
                  if (curChar == 48)
                     { jjCheckNAdd(16); }
                  break;
               case 32:
                  if (curChar == 54)
                     jjstateSet[jjnewStateCnt++] = 31;
                  break;
               case 33:
                  if (curChar == 49)
                     jjstateSet[jjnewStateCnt++] = 34;
                  break;
               case 34:
                  if ((0x7000000000000L & l) != 0L)
                     { jjCheckNAdd(10); }
                  break;
               case 36:
                  if ((0x3ff000100000000L & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  jjstateSet[jjnewStateCnt++] = 36;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 25)
                     kind = 25;
                  { jjCheckNAdd(36); }
                  break;
               case 16:
                  if (curChar == 104)
                     { jjAddStates(4, 5); }
                  break;
               case 19:
                  if (curChar == 109)
                     { jjAddStates(6, 7); }
                  break;
               case 22:
                  if (curChar == 115)
                     jjstateSet[jjnewStateCnt++] = 23;
                  break;
               case 26:
                  if (curChar == 90 && kind > 20)
                     kind = 20;
                  break;
               case 36:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 26)
                     kind = 26;
                  { jjCheckNAdd(36); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 37 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   8, 33, 14, 32, 17, 30, 20, 28, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", 
"\120\122\117\112\105\103\124\137\104\105\106\111\116\111\124\111\117\116", "\124\101\123\113\123", "\124\101\113\105", "\125\123\105\122", 
"\120\122\117\112\105\103\124", "\107\105\124\137\120\122\117\112\105\103\124\123", 
"\107\105\124\137\120\122\117\112\105\103\124", "\105\130\111\124", "\114\105\101\126\105", 
"\122\105\107\111\123\124\105\122", null, null, null, null, null, null, null, null, null, null, null, "\73", 
"\72", null, null, null, null, };
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedPos == 0 && jjmatchedKind > 27)
   {
      jjmatchedKind = 27;
   }
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
         matchedToken = jjFillToken();
         return matchedToken;
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

    /** Constructor. */
    public ParserTokenManager(SimpleCharStream stream){

      if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

    input_stream = stream;
  }

  /** Constructor. */
  public ParserTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream)
  {
    jjmatchedPos = jjnewStateCnt = 0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 37; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream, int lexState)
  {
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public void SwitchTo(int lexState)
  {
    if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[37];
    private final int[] jjstateSet = new int[2 * 37];

    
    protected char curChar;
}
