/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.announcements.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * @author Erik A. Olsson (eolsson@uci.edu)
 * 
 * $LastChangedBy$
 * $LastChangedDate$
 */
public class Topic {
													/* Announcements for this topic are... */
	public static final int PUSHED_FORCED = 1;		/* ...Pushed to the audience members and they cannot unsubscribe */
	public static final int PUSHED_INITIAL = 2; 	/* ...Pushed initially, but users can unsubscribe */
	public static final int PULLED = 3;				/* ...Not pushed to anybody, but target audience members can subscribe (pull) if they want to */
	public static final int EMERGENCY = 4;			/* A topic that supercedes all other topics */
	
	private static final org.apache.log4j.Logger logger = Logger
			.getLogger(Topic.class);
			
	private Set<Announcement> announcements;
	
	private Set<String> admins;
	private Set<String> moderators;
	private Set<String> authors;
	private Set<String> audience;
	
	private String creator;
	private String title;
	private String description;
	private boolean allowRss;
	private int subscriptionMethod;
	private Long id;

	public Topic() {
		admins = new TreeSet<String>();
		moderators = new TreeSet<String>();
		authors = new TreeSet<String>();
		audience = new TreeSet<String>();
	}

	
	public Set<String> getGroup(String key) {
		if (key.compareTo("admins") == 0) {
			return getAdmins();
		}
		else if (key.compareTo("moderators") == 0) {
			return getModerators();
		}
		else if (key.compareTo("authors") == 0) {
			return getAuthors();
		}
		else {
			return getAudience();
		}
	}
	
	public void setGroup(String key, Set<String> members) {
		if (key.compareTo("admins") == 0) {
			setAdmins(members);
		}
		else if (key.compareTo("moderators") == 0) {
			setModerators(members);
		}
		else if (key.compareTo("authors") == 0) {
			setAuthors(members);
		}
		else {
			setAudience(members);
		}
	}

	public boolean hasId() {
		return (this.id != null);
	}
	
	/**
	 * @return the moderators
	 */
	public Set<String> getModerators() {
		return moderators;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the allowRss
	 */
	public boolean isAllowRss() {
		return allowRss;
	}

	/**
	 * @param moderators the moderators to set
	 */
	public void setModerators(Set<String> moderators) {
		this.moderators = moderators;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param allowRss the allowRss to set
	 */
	public void setAllowRss(boolean allowRss) {
		this.allowRss = allowRss;
	}
	
	/**
	 * Returns a list of all announcements in this topic, regardless of status.
	 * @return the announcements
	 */
	public Set<Announcement> getAnnouncements() {
		return announcements;
	}

	/**
	 * Returns a list of all published announcements in this topic. For topics to be included in this
	 * list, they must also be within their specified display period.
	 * @return the announcements
	 */
	public Set<Announcement> getPublishedAnnouncements() {
		Set<Announcement> announcementsFiltered = new TreeSet<Announcement>();
		Date now = new Date();
		if (this.announcements != null) {
			for (Announcement ann : this.announcements) {
				if (ann.isPublished() && ann.getStartDisplay().before(now)
						&& ann.getEndDisplay().after(now)) {
					announcementsFiltered.add(ann);
				}
			}
		}
		return announcementsFiltered;
	}

	/**
	 * Returns a list of all published announcements in this topic. For topics
	 * to be included in this list, they must also be within their specified
	 * display period which is the window from yesterday to 30 days ago.
	 * 
	 * @return the announcements
	 */
	public Set<Announcement> getHistoricAnnouncements() {
		Set<Announcement> announcementsFiltered = new TreeSet<Announcement>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1); // subtract 1 day from today.
		Date now = cal.getTime();
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30); // subtract 30 days from today.
		Date then = cal.getTime();
		if (this.announcements != null) {
			for (Announcement ann : this.announcements) {
				if (logger.isInfoEnabled()) {
					logger.info("hist title: " + ann.getTitle() + "\n"
							+ "published? " + ann.isPublished() + "\n"
							+ "now: " + now + "\n" + "then: " + then + "\n"
							+ "startDisplay: " + ann.getStartDisplay() + "\n"
							+ "enDisplay: " + ann.getEndDisplay() + "\n"
							+ "before now? "
							+ ann.getStartDisplay().before(now) + "\n"
							+ "after then? " + ann.getEndDisplay().after(then));
				}
				if (ann.getEndDisplay().before(now)
						&& ann.getEndDisplay().after(then)) {
					announcementsFiltered.add(ann);
				}
			}
		}
		return announcementsFiltered;
	}
	
	/**
	 * Get the current number of displaying announcements
	 * @return
	 */
	public int getDisplayingAnnouncementCount() {
		return getPublishedAnnouncements().size();
	}
	
	/**
	 * Get the current number of approved & scheduled announcements
	 * @return
	 */
	public int getScheduledAnnouncementCount() {
		int count = 0;
		Date now = new Date();
		if (this.announcements != null) {
			for (Announcement ann: this.announcements) {
				if (ann.isPublished() &&
						ann.getStartDisplay().after(now)) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Get the current number of pending announcements
	 * @return
	 */
	public int getPendingAnnouncementCount() {
		int count = 0;
		if (this.announcements != null) {
			for (Announcement ann: this.announcements) {
				if (!ann.isPublished()) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param announcements the announcements to set
	 */
	public void setAnnouncements(Set<Announcement> announcements) {
		this.announcements = announcements;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the authors
	 */
	public Set<String> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(Set<String> authors) {
		this.authors = authors;
	}

	/**
	 * @return the admins
	 */
	public Set<String> getAdmins() {
		return admins;
	}

	/**
	 * @return the audience
	 */
	public Set<String> getAudience() {
		return audience;
	}

	/**
	 * @param admins the admins to set
	 */
	public void setAdmins(Set<String> admins) {
		this.admins = admins;
	}

	/**
	 * @param audience the audience to set
	 */
	public void setAudience(Set<String> audience) {
		this.audience = audience;
	}

	/**
	 * @return the subscriptionMethod
	 */
	public int getSubscriptionMethod() {
		return subscriptionMethod;
	}

	/**
	 * @param subscriptionMethod the subscriptionMethod to set
	 */
	public void setSubscriptionMethod(int subscriptionMethod) {
		this.subscriptionMethod = subscriptionMethod;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Topic t = (Topic) obj;
		return (t.getId().compareTo(this.id) == 0);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Topic [allowRss=" + allowRss + ", creator=" + creator
				+ ", description=" + description + ", id=" + id
				+ ", moderators=" + moderators + ", subscriptionMethod="
				+ subscriptionMethod + ", title=" + title + "]";
	}
	
	
	
}
