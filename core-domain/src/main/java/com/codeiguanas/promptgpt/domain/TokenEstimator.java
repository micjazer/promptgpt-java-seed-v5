
package com.codeiguanas.promptgpt.domain;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;

public final class TokenEstimator {
    private static final Encoding cl100k = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.CL100K_BASE);
    public static int estimate(String model, String text) {
        if (text == null || text.isEmpty()) return 0;
        try { return cl100k.countTokens(text); }
        catch (Throwable t) { return Math.max(1, text.length() / 4); }
    }
}
