package at.tugraz.ist.catroid.tutorial.tasks;

import java.util.HashMap;

import at.tugraz.ist.catroid.tutorial.ClickDispatcher;
import at.tugraz.ist.catroid.tutorial.SurfaceObjectTutor;

public class TaskNotification implements Task {
	private Tutor tutorType;
	private Notification notificationType;
	private String notificationString;

	public String getNotificationString() {
		return notificationString;
	}

	public void setNotificationString(String notificationString) {
		this.notificationString = notificationString;
	}

	public Notification getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Notification notificationType) {
		this.notificationType = notificationType;
	}

	@Override
	public Type getType() {
		return (Type.NOTIFICATION);
	}

	@Override
	public Tutor getTutorType() {
		return tutorType;
	}

	public void setTutorType(Tutor tutorType) {
		this.tutorType = tutorType;
	}

	@Override
	public boolean execute(HashMap<Task.Tutor, SurfaceObjectTutor> tutors) {
		ClickDispatcher clickDispatcher = new ClickDispatcher();
		clickDispatcher.processNotification(this);

		// TODO: Maybe Problem: User presses ProjectListItem between setting Notification at
		// begin of execute and the specific setCurrentNotification with the notificationString

		if (notificationType == Notification.PROJECT_ADD_SPRITE) {
			return true;
		}

		if (notificationType == Notification.CURRENT_PROJECT_BUTTON) {
			return true;
		}

		if (notificationType == Notification.PROJECT_LIST_ITEM) {
			//clickDispatcher.setCurrentNotification(notificationType, notificationString);
			return true;
		}

		if (notificationType == Notification.SCRIPTS_ADD_BRICK) {

			return true;
		}

		if (notificationType == Notification.BRICK_DIALOG_DONE) {
			return true;
		}

		if (notificationType == Notification.PROJECT_STAGE_BUTTON) {
			return true;
		}

		if (notificationType == Notification.TAB_COSTUMES) {
			return true;
		}

		if (notificationType == Notification.PROJECT_HOME_BUTTON) {
			return true;
		}

		if (notificationType == Notification.COSTUMES_ADD_COSTUME) {
			return true;
		}

		if (notificationType == Notification.COSTUMES_COPY) {
			return true;
		}

		if (notificationType == Notification.NEW_PROJECT_BUTTON) {
			return true;
		}

		// All tasks which result in a switch to another activity, just wait for a dummy
		// notification, which will be deleted during pause/resume of the Tutorial
		return true;
	}

	@Override
	public void setEndPositionOfTaskForTutor(HashMap<Tutor, SurfaceObjectTutor> tutors) {
	}
}
