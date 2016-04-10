/*
 * Projects ::= [1] SEQUENCE {
 *
 * }
 *
 */

import net.ddp2p.ASN1.*;
import java.util.Calendar;
import java.math.BigInteger;
import java.lang.Cloneable;

class ASNProjects extends ASNObj  implements Cloneable {

    @Override
    public Encoder getEncoder() {

        Encoder task = new Encoder()
        .initSequence();

        return task.setASN1Type(Encoder.CLASS_CONTEXT);
    }

    @Override
    public ASNProjects decode (Decoder dec) throws ASN1DecoderFail{
        Decoder decoder = dec.getContent();

        if (decoder.getTypeByte() != 0) throw new ASN1DecoderFail("Extra objects!");
        return this;
    }

    @Override
    public Object clone() {
        try {
            return new ASNProjects();
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
        ASNProjects projects = new ASNProjects();
        byte[] msg = projects.encode();
        Decoder decoder = new Decoder(msg);
        ASNProjects tmp = new ASNProjects();
        System.out.println(tmp.decode(decoder).toString());       
    }
}
