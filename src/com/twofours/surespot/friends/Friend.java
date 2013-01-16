package com.twofours.surespot.friends;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Friend implements Comparable<Friend> {
	// public static final int FRIEND = 1;
	public static final int NEW_FRIEND = 2;
	public static final int INVITED = 4;
	public static final int CHAT_ACTIVE = 8;
	public static final int INVITER = 16;

	private static final String TAG = "Friend";

	private String mName;
	private int mFlags;
	private int mMessageCount;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public void setChatActive(boolean set) {
		if (set) {
			mFlags |= CHAT_ACTIVE;
		} else {
			mFlags &= ~CHAT_ACTIVE;
		}
	}

	public void setInviter(boolean set) {
		if (set) {
			mFlags |= INVITER;
		} else {
			mFlags &= ~INVITER;
		}
	}

	public boolean isInviter() {
		return (mFlags & INVITER) == INVITER;
	}

	public void setInvited(boolean set) {
		if (set) {
			mFlags |= INVITED;
		} else {
			mFlags &= ~INVITED;
		}
	}

	public boolean isInvited() {
		return (mFlags & INVITED) == INVITED;
	}

	public void setNewFriend(boolean set) {
		if (set) {
			mFlags |= NEW_FRIEND;
			mFlags &= ~INVITED;
			mFlags &= ~INVITER;
		} else {
			mFlags &= ~NEW_FRIEND;
		}
	}

	public boolean isNewFriend() {
		return (mFlags & NEW_FRIEND) == NEW_FRIEND;
	}

	public boolean isFriend() {
		return (!isInvited() && !isInviter());
	}

	public int getFlags() {
		return mFlags;
	}

	public Integer getMessageCount() {
		return mMessageCount;
	}

	public synchronized void incMessageCount(int messageCount) {
		mMessageCount += messageCount;
		Log.v(TAG, "newCount: " + mMessageCount);
	}

	public synchronized void setMessageCount(int messageCount) {
		mMessageCount = messageCount;
	}

	public boolean isChatActive() {
		return (mFlags & CHAT_ACTIVE) == CHAT_ACTIVE;
	}

	@Override
	public int compareTo(Friend another) {
		// if the flags are the same sort by name
		// not active or invite, sort by name
		if ((another.getFlags() == this.getFlags())
				|| (another.getFlags() < CHAT_ACTIVE && this.getFlags() < CHAT_ACTIVE)) {
			return this.getName().compareTo(another.getName());
		} else {
			//sort by flag value
			return Integer.valueOf(another.getFlags()).compareTo(this.getFlags());
		}

	}

	public static Friend toFriend(JSONObject jsonFriend) throws JSONException {
		Friend friend = new Friend();

		String status = jsonFriend.getString("status");

		int flags = 0;
		// if (status.equals("friend")) {
		// friend.set
		// } else {
		if (status.equals("invited")) {
			friend.setInvited(true);
		}

		else {
			if (status.equals("invitee")) {
				friend.setInviter(true);
			}
		}
		// }

		friend.setName(jsonFriend.getString("name"));
		return friend;
	}
};
