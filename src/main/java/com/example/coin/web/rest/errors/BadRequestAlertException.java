package com.example.coin.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author Samuel Luo
 */
public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public BadRequestAlertException(String title) {
        super(null, title);}
}
