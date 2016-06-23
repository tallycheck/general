package com.taoswork.tallycheck.general.solution.cryptology;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Gao Yuan on 2016/6/23.
 */
public class BlockCipherInputStream extends FilterInputStream {

    private Cipher cipher;
    private InputStream input;
    private final int blockSize;
    private byte[] ibuffer;
    private int ibufferLen;
    private int ibufferFilled = 0;

    private boolean done = false;
    private byte[] obuffer;
    private int ostart = 0;
    private int ofinish = 0;
    private boolean closed = false;
    private boolean read = false;

    public BlockCipherInputStream(InputStream in, Cipher cipher, int blockSize) {
        super(in);
        this.input = in;
        this.cipher = cipher;
        this.blockSize = blockSize;
        ibufferLen = Math.max(512, blockSize * 8);
        ibuffer = new byte[ibufferLen];
    }

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected BlockCipherInputStream(InputStream in) {
        super(in);
        this.input = in;
        this.cipher = new NullCipher();
        this.blockSize = 64;
    }

    private byte[] processInputBuffer(boolean eof) throws BadPaddingException, IllegalBlockSizeException {
        final int outputSizeUnit = this.cipher.getOutputSize(1);
        int blocks = ibufferFilled / blockSize;
        int pendingLen = ibufferFilled % blockSize;
        int outputSize = outputSizeUnit * blocks;
        if(eof && pendingLen > 0)
            outputSize += outputSizeUnit;
        byte[] output = new byte[outputSize];
        int inputHandled = 0;
        int outputFilled = 0;
        if(blocks > 0){
            for (int b = 0 ; b < blocks ; ++b){
                byte[] bytes = this.cipher.doFinal(this.ibuffer, inputHandled, blockSize);
                System.arraycopy(bytes, 0, output, outputFilled, bytes.length);
                inputHandled += blockSize;
                outputFilled += bytes.length;
            }
        }

        if(eof && pendingLen > 0){
            byte[] bytes = this.cipher.doFinal(this.ibuffer, inputHandled, pendingLen);
            System.arraycopy(bytes, 0, output, outputFilled, bytes.length);
            inputHandled += pendingLen;
            outputFilled += bytes.length;
        }
        if(pendingLen > 0){
            System.arraycopy(this.ibuffer, blocks * blockSize, this.ibuffer, 0, pendingLen);
        }
        this.ibufferFilled = pendingLen;
        if(outputFilled != output.length){
            byte[] output2 = new byte[outputFilled];
            System.arraycopy(output, 0, output2, 0, outputFilled);
            return output2;
        }
        return output;
    }

    private int getMoreData() throws IOException {
        if(this.done) {
            return -1;
        } else {
            int got = this.input.read(this.ibuffer, ibufferFilled, ibufferLen - ibufferFilled);
            this.read = true;
            if(got == -1) {
                this.done = true;

                try {
                    this.obuffer = processInputBuffer(true);
                } catch (BadPaddingException | IllegalBlockSizeException e) {
                    this.obuffer = null;
                    throw new IOException(e);
                }

                if(this.obuffer == null) {
                    return -1;
                } else {
                    this.ostart = 0;
                    this.ofinish = this.obuffer.length;
                    return this.ofinish;
                }
            } else {
                try {
                    this.ibufferFilled += got;
                    this.obuffer = processInputBuffer(false);
                } catch (BadPaddingException | IllegalBlockSizeException e) {
                    this.obuffer = null;
                    throw new IOException(e);
                }

                this.ostart = 0;
                if(this.obuffer == null) {
                    this.ofinish = 0;
                } else {
                    this.ofinish = this.obuffer.length;
                }

                return this.ofinish;
            }
        }
    }

    public int read() throws IOException {
        if(this.ostart >= this.ofinish) {
            int got;
            for(got = 0; got == 0; got = this.getMoreData()) {
                ;
            }

            if(got == -1) {
                return -1;
            }
        }

        return this.obuffer[this.ostart++] & 255;
    }

    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if(this.ostart >= this.ofinish) {
            int got;
            for(got = 0; got == 0; got = this.getMoreData()) {
                ;
            }

            if(got == -1) {
                return -1;
            }
        }

        if(len <= 0) {
            return 0;
        } else {
            int olen = this.ofinish - this.ostart;
            if(len < olen) {
                olen = len;
            }

            if(b != null) {
                System.arraycopy(this.obuffer, this.ostart, b, off, olen);
            }

            this.ostart += olen;
            return olen;
        }
    }

    /**
     * Skips over and discards <code>n</code> bytes of data from the
     * input stream. The <code>skip</code> method may, for a variety of
     * reasons, end up skipping over some smaller number of bytes,
     * possibly <code>0</code>. The actual number of bytes skipped is
     * returned.
     * <p>
     * This method simply performs <code>in.skip(n)</code>.
     *
     * @param      n   the number of bytes to be skipped.
     * @return     the actual number of bytes skipped.
     * @exception  IOException  if the stream does not support seek,
     *                          or if some other I/O error occurs.
     */
    public long skip(long n) throws IOException {
        int olen = this.ofinish - this.ostart;
        if(n > (long)olen) {
            n = (long)olen;
        }

        if(n < 0L) {
            return 0L;
        } else {
            this.ostart = (int)((long)this.ostart + n);
            return n;
        }
    }

    public int available() throws IOException {
        return this.ofinish - this.ostart;
    }

    public void close() throws IOException {
        if(!this.closed) {
            this.closed = true;
            this.input.close();

            try {
                if(!this.done) {
                    processInputBuffer(true);
                }
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                if(this.read) {
                    throw new IOException(e);
                }
            }

            this.ostart = 0;
            this.ofinish = 0;
        }
    }

    public boolean markSupported() {
        return false;
    }

}
