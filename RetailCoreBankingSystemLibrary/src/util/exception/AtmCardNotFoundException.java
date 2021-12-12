/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author kaushikr
 */
public class AtmCardNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AtmCardNotFoundException</code> without
     * detail message.
     */
    public AtmCardNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AtmCardNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AtmCardNotFoundException(String msg) {
        super(msg);
    }
}
