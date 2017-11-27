/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.android.systemui.car;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import com.android.internal.logging.MetricsLogger;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.statusbar.ExpandableNotificationRow;
import com.android.systemui.statusbar.NotificationData;
import com.android.systemui.statusbar.NotificationEntryManager;
import com.android.systemui.statusbar.NotificationGutsManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.VisualStabilityManager;
import com.android.systemui.statusbar.phone.NotificationGroupManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;

public class CarNotificationEntryManager extends NotificationEntryManager {
    public CarNotificationEntryManager(
            NotificationLockscreenUserManager lockscreenUserManager,
            NotificationGroupManager groupManager,
            NotificationGutsManager gutsManager,
            NotificationRemoteInputManager remoteInputManager,
            NotificationMediaManager mediaManager,
            ForegroundServiceController foregroundServiceController,
            NotificationListener notificationListener,
            MetricsLogger metricsLogger,
            DeviceProvisionedController deviceProvisionedController,
            VisualStabilityManager visualStabilityManager,
            UiOffloadThread uiOffloadThread, Context context) {
        super(lockscreenUserManager, groupManager, gutsManager, remoteInputManager, mediaManager,
                foregroundServiceController, notificationListener, metricsLogger,
                deviceProvisionedController, visualStabilityManager, uiOffloadThread, context);
    }

    /**
     * Returns the
     * {@link com.android.systemui.statusbar.ExpandableNotificationRow.LongPressListener} that will
     * be triggered when a notification card is long-pressed.
     */
    @Override
    public ExpandableNotificationRow.LongPressListener getNotificationLongClicker() {
        // For the automative use case, we do not want to the user to be able to interact with
        // a notification other than a regular click. As a result, just return null for the
        // long click listener.
        return null;
    }

    @Override
    public boolean shouldPeek(NotificationData.Entry entry, StatusBarNotification sbn) {
        // Because space is usually constrained in the auto use-case, there should not be a
        // pinned notification when the shade has been expanded. Ensure this by not pinning any
        // notification if the shade is already opened.
        if (!mPresenter.isPresenterFullyCollapsed()) {
            return false;
        }

        return super.shouldPeek(entry, sbn);
    }
}
