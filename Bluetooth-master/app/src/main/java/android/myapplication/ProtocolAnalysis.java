package android.myapplication;

import android.util.Log;

import static android.content.ContentValues.TAG;

public class ProtocolAnalysis {

    static String[] waveLength = {
            "C1 400 410 420",
            "C2 430 440 450",
            "C3 460 470 480",
            "C4 500 510 520",
            "C5 540 550 560",
            "C6 573 583 593",
            "C7 610 620 630",
            "C8 660 670 680",
            "C9 000 000 000",
            "CA 000 000 000",
            "CB 000 000 000",
            "CC 000 000 000"
    };

    //01 03 00 18 001300BA001F000C000C0008000C000500A10000000001AC 39 B5
    public static void print(byte[] src){
        if( src.length < 5){
            return;
        }
        byte cmd = src[1];
        if(cmd == 0x03){//读取到波长

            String hexString  = HexCode.bytesToHexStringSpace(src);
            System.out.println( "Analysis:"  + hexString);
            int dataLen =  toUnsigned(src[2]) * 256  +  toUnsigned( src[3] );//数据长度
            if(dataLen % 2 != 0){
                return;
            }


            for( int i = 0; i < dataLen ; i+=2){
                int pos =  i + 4;
                int value = toUnsigned( src[pos] ) * 256 + toUnsigned( src[pos+1] );
                System.out.println( "Analysis:" +  waveLength[i/2]  +  ":"   +  value);
            }


        }else if( cmd == 0x05){

        }

    }


    static int toUnsigned(short s) {
        return s & 0x0FFFF;
    }

    static int toUnsigned(byte s) {
        return s & 0x0FF;
    }
}
