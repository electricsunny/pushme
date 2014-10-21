package com.example.funkatron.twofa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by electricSunny on 14/10/2014.
 */

public class TwoFactorAuthenticationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // send 'authorise' result back to TeleSign
    }

}
