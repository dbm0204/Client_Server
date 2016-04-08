/*
 * GetProject ::= [1] SEQUENCE {
 *
 * }
 *
 */

import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNGetProject extends ASNObj  implements Cloneable {

    @Override
    public Encoder getEncoder() {

        Encoder task = new Encoder()
        .initSequence();

        return task.setASN1Type(Encoder.CLASS_CONTEXT);
    }

    @Override
    public ASNGetProject decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();

        return this;
    }

    @Override
    public Object clone() {
        try {
            return new ASNGetProject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    public static void main (String[] args) throws ASN1DecoderFail {
        ASNGetProject projects = new ASNGetProject();
        byte[] msg = projects.encode();
        Decoder decoder = new Decoder(msg);
        ASNGetProject tmp = new ASNGetProject();
        System.out.println(tmp.decode(decoder).toString());       
    }
}
