package com.github.lbam.dcBot.Handlers;

import java.awt.Color;

import com.github.lbam.dcBot.BotMain;
import com.github.lbam.dcBot.Commands.Callback;
import com.github.lbam.dcBot.Commands.GameReceiver;
import com.github.lbam.dcBot.Database.DAO.DaoPreferences;
import com.github.lbam.dcBot.Interfaces.Command;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

public class EventHandler {
	
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event){
		System.out.println("Entrou");
	}
	
	@EventSubscriber
	public void onGuildCreateEvent(GuildCreateEvent event) {
		IGuild server = event.getGuild();
		String serverRegion = server.getRegion().getName();
		
		for(IChannel ch : server.getChannels()) {
			MessageHandler.sendMessage("Saudações, invocador", "Comandos: \n %dc jogar - inicia um jogo. \n %dc sair - encerra um jogo. \n %dc ajuda - reabre esse painel. \n Para pedir dicas, digite 'dica' enquanto em uma sessão. Mas lembre-se: cada jogador poderá utilizar apenas 3 dicas.", Color.yellow, ch);
		}
		
		DaoPreferences database = new DaoPreferences();
		if(!database.existeRegistro(server.getID())){
			if(serverRegion.equals("Brazil")){
				database.createPreferences(server.getID(), "br");
			}else{
				database.createPreferences(server.getID(), "us");
				try {
					BotMain.Bot.changeUsername("Who is that champ?");
				} catch (Exception e){
					System.out.println("Não pude mudar o nick :(");
				}
			}
		}
		
	}
	@EventSubscriber
	public void onMessageEvent(MessageReceivedEvent event){
		IMessage message = event.getMessage();
		String[] args = message.getContent().split(" ");
		
		if(message.getAuthor().isBot())
			return;

		if(args[0].equals("%dc") && args.length > 1) {
			Command cmd = new Callback(new GameReceiver(message.getAuthor(),message.getChannel()), args[1], message.getChannel());
			cmd.execute();
		} 
		else
			return;
		
	}
}
