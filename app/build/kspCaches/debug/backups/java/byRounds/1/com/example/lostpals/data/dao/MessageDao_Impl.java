package com.example.lostpals.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.lostpals.data.dto.InboxConversationData;
import com.example.lostpals.data.entity.Message;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Message> __insertionAdapterOfMessage;

  public MessageDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessage = new EntityInsertionAdapter<Message>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `messages` (`id`,`postId`,`senderId`,`receiverId`,`text`,`photoUri`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Message value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getPostId());
        stmt.bindLong(3, value.getSenderId());
        stmt.bindLong(4, value.getReceiverId());
        if (value.getText() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getText());
        }
        if (value.getPhotoUri() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPhotoUri());
        }
        stmt.bindLong(7, value.getTimestamp());
      }
    };
  }

  @Override
  public Object insert(final Message message, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfMessage.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getMessagesForPost(final long postId,
      final Continuation<? super List<Message>> continuation) {
    final String _sql = "SELECT * FROM messages WHERE postId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, postId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Message>>() {
      @Override
      public List<Message> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPostId = CursorUtil.getColumnIndexOrThrow(_cursor, "postId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Message> _result = new ArrayList<Message>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Message _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPostId;
            _tmpPostId = _cursor.getLong(_cursorIndexOfPostId);
            final long _tmpSenderId;
            _tmpSenderId = _cursor.getLong(_cursorIndexOfSenderId);
            final long _tmpReceiverId;
            _tmpReceiverId = _cursor.getLong(_cursorIndexOfReceiverId);
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Message(_tmpId,_tmpPostId,_tmpSenderId,_tmpReceiverId,_tmpText,_tmpPhotoUri,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getMessagesForConversation(final long meId, final long otherId, final long postId,
      final Continuation<? super List<Message>> continuation) {
    final String _sql = "\n"
            + "    SELECT * FROM messages \n"
            + "    WHERE ((senderId = ? AND receiverId = ?) \n"
            + "    OR (senderId = ? AND receiverId = ?)) \n"
            + "    AND postId = ? \n"
            + "    ORDER BY timestamp ASC\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, meId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, otherId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, otherId);
    _argIndex = 4;
    _statement.bindLong(_argIndex, meId);
    _argIndex = 5;
    _statement.bindLong(_argIndex, postId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Message>>() {
      @Override
      public List<Message> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPostId = CursorUtil.getColumnIndexOrThrow(_cursor, "postId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Message> _result = new ArrayList<Message>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Message _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPostId;
            _tmpPostId = _cursor.getLong(_cursorIndexOfPostId);
            final long _tmpSenderId;
            _tmpSenderId = _cursor.getLong(_cursorIndexOfSenderId);
            final long _tmpReceiverId;
            _tmpReceiverId = _cursor.getLong(_cursorIndexOfReceiverId);
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Message(_tmpId,_tmpPostId,_tmpSenderId,_tmpReceiverId,_tmpText,_tmpPhotoUri,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getInboxConversations(final long userId,
      final Continuation<? super List<InboxConversationData>> continuation) {
    final String _sql = "SELECT m.*, otherUser.username, otherUser.photoUri AS userPhotoUri\n"
            + "    FROM messages m\n"
            + "    INNER JOIN users otherUser ON\n"
            + "        (CASE \n"
            + "            WHEN m.senderId = ? THEN otherUser.id = m.receiverId\n"
            + "            ELSE otherUser.id = m.senderId\n"
            + "        END)\n"
            + "    WHERE (m.senderId = ? OR m.receiverId = ?)\n"
            + "      AND m.timestamp = (\n"
            + "        SELECT MAX(latestMessage.timestamp)\n"
            + "        FROM messages latestMessage\n"
            + "        WHERE (\n"
            + "            (latestMessage.senderId = ? AND latestMessage.receiverId = \n"
            + "                CASE WHEN m.senderId = ? THEN m.receiverId ELSE m.senderId END)\n"
            + "            OR\n"
            + "            (latestMessage.receiverId = ? AND latestMessage.senderId = \n"
            + "                CASE WHEN m.senderId = ? THEN m.receiverId ELSE m.senderId END)\n"
            + "        )\n"
            + "    )\n"
            + "    ORDER BY m.timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 7);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 4;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 5;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 6;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 7;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<InboxConversationData>>() {
      @Override
      public List<InboxConversationData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPostId = CursorUtil.getColumnIndexOrThrow(_cursor, "postId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfUserPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUri");
          final List<InboxConversationData> _result = new ArrayList<InboxConversationData>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final InboxConversationData _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPostId;
            _tmpPostId = _cursor.getLong(_cursorIndexOfPostId);
            final long _tmpSenderId;
            _tmpSenderId = _cursor.getLong(_cursorIndexOfSenderId);
            final long _tmpReceiverId;
            _tmpReceiverId = _cursor.getLong(_cursorIndexOfReceiverId);
            final String _tmpText;
            if (_cursor.isNull(_cursorIndexOfText)) {
              _tmpText = null;
            } else {
              _tmpText = _cursor.getString(_cursorIndexOfText);
            }
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpUserPhotoUri;
            if (_cursor.isNull(_cursorIndexOfUserPhotoUri)) {
              _tmpUserPhotoUri = null;
            } else {
              _tmpUserPhotoUri = _cursor.getString(_cursorIndexOfUserPhotoUri);
            }
            _item = new InboxConversationData(_tmpId,_tmpPostId,_tmpSenderId,_tmpReceiverId,_tmpText,_tmpPhotoUri,_tmpTimestamp,_tmpUsername,_tmpUserPhotoUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
