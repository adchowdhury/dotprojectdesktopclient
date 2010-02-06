package com.zycus.dotproject.api;

import java.util.Set;

import com.zycus.dotproject.bo.BOForum;
import com.zycus.dotproject.bo.BOForumMessage;
import com.zycus.dotproject.bo.BOUser;

public interface IForumManager {
	Set<BOForum> getForums(BOUser user);
	void postReply(BOUser user, BOForumMessage message);
}