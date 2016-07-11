package Messages;

import java.io.IOException;

import FileStorage.Commands;
import FileStorage.Mutes;
import Music.Voice;
import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.SubscribeEvent;

public class MessageEvent {
	@SubscribeEvent
	public void message(MessageReceivedEvent event) throws IOException {
		if (!Voice.isSetup) {
			Voice.setup(event.getGuild());
		}
		String msg = event.getMessage().getContent();
		String[] args = msg.split(" ");
		if (event.getChannel() instanceof PrivateChannel && !event.getAuthor().isBot()) {
			if (args[0].equalsIgnoreCase("/help")) {
				Commands.pmHelp(event.getAuthor()); return;
			}
			else if (args[0].equalsIgnoreCase("/helpop")) {
				if (msg.equalsIgnoreCase("/helpop") || msg.equalsIgnoreCase("/helpop ")) event.getChannel().sendMessage("Your helpop was empty! Please resend with content :P");
				else {
					String sb = msg.replace("/helpop ", "");
					event.getChannel().getJDA().getTextChannelsByName("discord_staff").get(0).sendMessage("```Helpop by " + event.getAuthorName() + ": " + sb + "```");
				}
			}
			else {
				event.getPrivateChannel().sendMessage("You cannot send this command in this channel!"); return;
			}
		}
		else {
			if (Mutes.isMuted("@" + event.getAuthor().getUsername())) {
				event.getAuthor().getPrivateChannel().sendMessage("```You're currently muted in this server! Please pm a staff member to negotiate an unmute or\nYou can do /helpop <msg> to pm staff members a message. If you spam or cause trouble, you may be banned as a result```");
				event.getMessage().deleteMessage();
				return;
			}
			if (args[0].equalsIgnoreCase("/online")) {
				int online = 0;
				for (User user : event.getGuild().getUsers()) {
					if (user.getOnlineStatus() == OnlineStatus.ONLINE || user.getOnlineStatus() == OnlineStatus.AWAY) online++;
				}
				event.getChannel().sendMessage("Online users: " + online);
			}	
			else if (args[0].equalsIgnoreCase("/members")) {
				if (msg.equalsIgnoreCase("/members list")) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < event.getGuild().getUsers().size(); i++) {
						StringBuilder usb = new StringBuilder();
						usb.append(event.getGuild().getUsers().get(i).getUsername());
						if (i < event.getGuild().getUsers().size() - 1) {
							usb.append(", ");
						}
						sb.append(usb);
					}
					event.getChannel().sendMessage("```*List of members*: " + sb.toString() + "```");
				}
				else {
					int members = event.getGuild().getUsers().size();
					event.getTextChannel().sendMessage("```Total members: " + members + "```");
				}
			}
			else if (args[0].equalsIgnoreCase("/status")) {
				event.getTextChannel().sendMessage("http://www.picgifs.com/graphics/o/ok/graphics-ok-134103.gif");
			}
			else if (args[0].equalsIgnoreCase("/list")) {
				Aids.list(event);	
			}
			else if (args[0].equalsIgnoreCase("/tag")) {
				TagHandler.passTag(event);
			}
			else if (args[0].equalsIgnoreCase("/help")) {
				Commands.pmHelp(event.getAuthor());
			}	
			else if (args[0].equalsIgnoreCase("/clear")) {
				ClearHandler.passClear(event, msg);
			}
			else if (args[0].equalsIgnoreCase("/todo")) {
				TodoHandler.passTodo(event, msg);
			}
			else if (args[0].equalsIgnoreCase("/mute")) {
				MuteHandler.passMute(event, msg);
			}
			else if (args[0].equalsIgnoreCase("/helpop") && !event.getAuthor().isBot()) {
				event.getAuthor().getPrivateChannel().sendMessage("/helpop may only be used in a private channel with me!");
			}
			else if (msg.equalsIgnoreCase("/music")) {
				event.getAuthor().getPrivateChannel().sendMessage("```Music Commands:\n/music nowplaying (current song)\n/music reset (resets player [don't use!])```");
			}
			else if (msg.equalsIgnoreCase("/music reset")) {
				event.getChannel().sendMessage("Reset music player!");
				Voice.reset();
			}	
			else if (msg.equalsIgnoreCase("/music nowplaying")) {
				Voice.nowPlaying(event.getGuild().getTextChannels().get(event.getGuild().getTextChannels().size() - 1));
			}
		}

	}
}
