package il.ac.tau.cs.hanukcoin;

    /*
     * Block is 36 byte/288bit long.
     * record/block format:
     * 32 bit serial number
     * 32 bit wallet number
     * 64 bit prev_sig[:8]highest bits  (first half ) of previous block's signature (including all the block)
     * 64 bit puzzle answer
     * 96 bit sig[:12] - md5 - the first 12 bytes of md5 of above fields. need to make last N bits zero
     */


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {
    public final int BLOCK_SZ = 36;
    public enum BlockError {OK, BAD_SERIAL_NO, SAME_WALLET_PREV, NO_PREV_SIG, SIG_NO_ZEROS, SIG_BAD}
    protected byte[] data;
    public int getSerialNumber() {
        return HanukCoinUtils.intFromBytes(data, 0);
    }
    public int getWalletNumber() {
        return HanukCoinUtils.intFromBytes(data, 4);
    }
    public static Block createNoSig(int serialNumber, int walletNumber, byte[] prevSig8) {
        Block b = new Block();
        b.data = new byte[36];
        HanukCoinUtils.intIntoBytes(b.data, 0, serialNumber);
        HanukCoinUtils.intIntoBytes(b.data, 4, walletNumber);
        System.arraycopy(prevSig8, 0, b.data, 8, 8);
        return b;
    }
    public static Block create(int serialNumber, int walletNumber, byte[] prevSig8, byte[] puzzle8, byte[] sig12) {
        Block b = createNoSig(serialNumber, walletNumber, prevSig8);
        System.arraycopy(sig12, 0, b.data, 24, 12);
        System.arraycopy(puzzle8, 0, b.data, 16, 8);
        return b;
    }

    /**
     * put 8 bytes dat into puzzle field
     * @param longPuzzle - 64 bit puzzle
     */
    public void setLongPuzzle(long longPuzzle) {
        // Treat it as 2 32bit integers
        HanukCoinUtils.intIntoBytes(data, 16, (int)(longPuzzle >> 32));
        HanukCoinUtils.intIntoBytes(data, 20, (int)(longPuzzle & 0xFFFFFFFF));
    }

    public void setSignaturePart(byte[] sig) {
        System.arraycopy(sig, 0, data, 24, 12);
    }



    public byte[] calcSignature() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // may cause NoSuchAlgorithmException
            md.update(data, 0, 24);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error - missing MD5");
        }
    }

    public byte[] getBytes() {
        return data;
    }

    public BlockError checkSignature() {
        byte[] sig = calcSignature();
        int serialNum = getSerialNumber();
        int nZeros = HanukCoinUtils.numberOfZerosForPuzzle(serialNum);
        if (!HanukCoinUtils.checkSignatureZeros(sig, nZeros)) {
            return BlockError.SIG_NO_ZEROS;
        }
        if (!HanukCoinUtils.ArraysPartEquals(12, data, 24, sig, 0)) {
            return BlockError.SIG_BAD;
        }
        return BlockError.OK;
    }

    public BlockError checkValidNext(Block prevBlock) {
        if (getSerialNumber() !=  prevBlock.getSerialNumber() + 1) {
            return BlockError.BAD_SERIAL_NO;  // bad serial number - should be prev + 1
        }
        if (getWalletNumber() == prevBlock.getWalletNumber()) {
            return BlockError.SAME_WALLET_PREV;  // don't allow two consequent blocks with same wallet
        }
        if (!HanukCoinUtils.ArraysPartEquals(8, data, 8, prevBlock.data, 24)) {
            return BlockError.NO_PREV_SIG;  // check prevSig field is indeed siganute of prev block
        }
        return checkSignature();
    }

    public String binDump() {
        String dump = "";
        for (int i = 0; i < BLOCK_SZ; i++) {
            if ((i % 4) == 0) {
                dump += " ";
            }
            if ((i % 8) == 0) {
                dump += "\n";
            }
            dump += String.format("%02X ", data[i]);
        }
        return dump;
    }


}

