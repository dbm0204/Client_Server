/**
 * ProjectOK ::= [1] SEQUENCE {
 *    event_id INTEGER
 *    event ASBObj
 * }
 *
 */

import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNEvent extends ASNObj implements Cloneable {
    public static final byte PROJECTOK=0;
    public static final byte GETPROJECT=1;
    public static final byte PROJECT=2;
    public static final byte PROJECTS=3;
    public static final byte PROJECTSANSWER=4;
    public static final byte TAKE=5;
    
    int eventId = -1;
    ASNObj event[];
    
    public ASNEvent () {
    }
    
    public ASNEvent (int eventId, ASNObj obj) {
        this.eventId = eventId;
        event = new ASNObj[1];
        event[0] = obj;
    }
    
    public ASNObj getEvent() {
        if (event != null) {
            return event[0];
        }
        return null;
    }
    
    public int getEventId() {
        return eventId;
    }
    
    @Override
    public Encoder getEncoder() {
    
        Encoder task = new Encoder()
        .initSequence()
        .addToSequence(new Encoder(eventId))
        .addToSequence(Encoder.getEncoder(event)
            .setASN1Type(Encoder.TAG_SEQUENCE));
        //task.print();
        
        return task.setASN1Type(Encoder.CLASS_UNIVERSAL);
    }

    @Override
    public ASNEvent decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();
        eventId = decoder.getFirstObject(true).getInteger(Encoder.TAG_INTEGER).intValue();
        
        if (eventId == PROJECTOK) {
            event = decoder.getFirstObject(true)
                .getSequenceOf (
                    Encoder.CLASS_UNIVERSAL,
                    new ASNProjectOK[0],
                    new ASNProjectOK());
        } else if (eventId == GETPROJECT) {
            event = decoder.getFirstObject(true)
                .getSequenceOf (
                    Encoder.CLASS_APPLICATION,
                    new ASNProject[0],
                    new ASNProject());       
        } else if (eventId == PROJECT) {
            event = decoder.getFirstObject(true)
                .getSequenceOf (
                    Encoder.CLASS_APPLICATION,
                    new ASNProject[0],
                    new ASNProject());     
        } else if (eventId == PROJECTS) {
            event = decoder.getFirstObject(true)
                .getSequenceOf (
                    Encoder.CLASS_CONTEXT,
                    new ASNProjects[0],
                    new ASNProjects());     
        } else if (eventId == PROJECTSANSWER) {
            event = decoder.getFirstObject(true)
                .getSequenceOf (
                    Encoder.CLASS_PRIVATE,
                    new ASNProjectsAnswer[0],
                    new ASNProjectsAnswer());      
        } else if (eventId == TAKE) {
            event = decoder.getFirstObject(true)
                .getSequenceOf (
                    Encoder.CLASS_UNIVERSAL,
                    new ASNTake[0],
                    new ASNTake());     
        }
        
        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }
    
    @Override
    public Object clone() {
        try {
            if (event != null) {
                return new ASNEvent(eventId, event[0]);
            } else {
                return new ASNEvent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Event ID: " + eventId + "\n" +
                "Event: " + event[0].toString() + "\n";
    }
    
    public static void main (String[] args) throws ASN1DecoderFail {
        int code = 0;
        ASNEvent write = new ASNEvent(code, new ASNProjectOK(2));
        
        byte[] msg = write.encode();
        
        Decoder decoder = new Decoder(msg);
        
        ASNEvent read = new ASNEvent();
        
        System.out.println(read.decode(decoder).toString());
        
    }
}
