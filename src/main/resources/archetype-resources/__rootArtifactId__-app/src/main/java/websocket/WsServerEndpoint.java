package ${package}.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.CloseReason;
#if($configType.contains('@'))
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
#else
import javax.websocket.Endpoint;
#end
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.PongMessage;
import javax.websocket.Session;
#if($configType.contains('@'))
import javax.websocket.server.ServerEndpoint;
#end

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

## // use @EnableWebSocket or <websocket> when spring-websocket imported.
## // one can implements ServerApplicationConfig as Endpoint filter.
@Slf4j
#if($configType.contains('@'))
@ServerEndpoint("/wss")
#end
public class WsServerEndpoint #if(!$configType.contains('@'))extends Endpoint #{end}{

    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

#if($configType.contains('@'))
    @OnOpen
#else
    @Override
#end
    public void onOpen(Session session, EndpointConfig config) {
        Session old = sessions.putIfAbsent(session.getId(), session);
        if(old != null) {
            log.warn("old session[id={}] not clear!", old.getId());
        }
        session.addMessageHandler(String.class, t-> {
            // TODO
//            try {
//                send(session.getId(), t, false);
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
        });
        session.addMessageHandler(ByteBuffer.class, t-> {
            // TODO
        });
## // cannot addMessageHandler for one type and has OnMessage annotation for one type at same time!
    }

#if($configType.contains('@'))
    @OnClose
#else
    @Override
#end
    public void onClose(Session session, CloseReason closeReason) {
        log.debug("session[id={}] closed for '{}'",session.getId(), closeReason);
        sessions.remove(session.getId());
    }

#if($configType.contains('@'))
    @OnError
#else
    @Override
#end
    public void onError(Session session, Throwable thr) {

    }

    public static void send(String id, Object msg, boolean async) throws IOException, EncodeException {
        Session session = sessions.get(id);
        if(session == null) {
            return;
        }
        if(msg instanceof String) {
            sendText(session, (String) msg, async);
        } else if(msg instanceof ByteBuffer) {
            sendBinary(session, (ByteBuffer) msg, async);
        } else {
            sendObject(session, msg, async);
        }
    }

    private static void sendText(Session session, String msg, boolean async) throws IOException, EncodeException {
        if(async) {
            session.getAsyncRemote().sendText(msg);
        } else {
            session.getBasicRemote().sendText(msg);
        }
    }

    private static void sendBinary(Session session, ByteBuffer msg, boolean async) throws IOException, EncodeException {
        if(async) {
            session.getAsyncRemote().sendBinary(msg);
        } else {
            session.getBasicRemote().sendBinary(msg);
        }
    }

    private static void sendObject(Session session, Object msg, boolean async) throws IOException, EncodeException {
        if(async) {
            session.getAsyncRemote().sendObject(msg);
        } else {
            session.getBasicRemote().sendObject(msg);
        }
    }

#if($configType.contains('@'))
    @OnMessage
#end
    public void onMessage(PongMessage message) {

    }
}
