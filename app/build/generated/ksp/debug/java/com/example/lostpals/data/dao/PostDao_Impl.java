package com.example.lostpals.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.lostpals.data.database.Converters;
import com.example.lostpals.data.dto.PostDisplayDto;
import com.example.lostpals.data.entity.Location;
import com.example.lostpals.data.entity.ObjectType;
import com.example.lostpals.data.entity.Post;
import com.example.lostpals.data.entity.PostType;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PostDao_Impl implements PostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Post> __insertionAdapterOfPost;

  private Converters __converters;

  private final EntityDeletionOrUpdateAdapter<Post> __deletionAdapterOfPost;

  private final EntityDeletionOrUpdateAdapter<Post> __updateAdapterOfPost;

  public PostDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPost = new EntityInsertionAdapter<Post>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `posts` (`id`,`ownerId`,`title`,`description`,`location`,`objectType`,`postType`,`photoUri`,`reward`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Post value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getOwnerId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        final String _tmp = __converters().fromLocation(value.getLocation());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp);
        }
        final String _tmp_1 = __converters().fromObjectType(value.getObjectType());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_1);
        }
        final String _tmp_2 = __converters().fromPostType(value.getPostType());
        if (_tmp_2 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, _tmp_2);
        }
        if (value.getPhotoUri() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getPhotoUri());
        }
        if (value.getReward() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindDouble(9, value.getReward());
        }
        stmt.bindLong(10, value.getTimestamp());
      }
    };
    this.__deletionAdapterOfPost = new EntityDeletionOrUpdateAdapter<Post>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `posts` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Post value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfPost = new EntityDeletionOrUpdateAdapter<Post>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `posts` SET `id` = ?,`ownerId` = ?,`title` = ?,`description` = ?,`location` = ?,`objectType` = ?,`postType` = ?,`photoUri` = ?,`reward` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Post value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getOwnerId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
        final String _tmp = __converters().fromLocation(value.getLocation());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp);
        }
        final String _tmp_1 = __converters().fromObjectType(value.getObjectType());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, _tmp_1);
        }
        final String _tmp_2 = __converters().fromPostType(value.getPostType());
        if (_tmp_2 == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, _tmp_2);
        }
        if (value.getPhotoUri() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getPhotoUri());
        }
        if (value.getReward() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindDouble(9, value.getReward());
        }
        stmt.bindLong(10, value.getTimestamp());
        stmt.bindLong(11, value.getId());
      }
    };
  }

  @Override
  public Object insert(final Post post, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfPost.insertAndReturnId(post);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deletePost(final Post post, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPost.handle(post);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updatePost(final Post post, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPost.handle(post);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getPostById(final long id, final Continuation<? super Post> continuation) {
    final String _sql = "SELECT * FROM posts WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Post>() {
      @Override
      public Post call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfObjectType = CursorUtil.getColumnIndexOrThrow(_cursor, "objectType");
          final int _cursorIndexOfPostType = CursorUtil.getColumnIndexOrThrow(_cursor, "postType");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfReward = CursorUtil.getColumnIndexOrThrow(_cursor, "reward");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final Post _result;
          if(_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOwnerId;
            _tmpOwnerId = _cursor.getLong(_cursorIndexOfOwnerId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Location _tmpLocation;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfLocation);
            }
            final Location _tmp_1 = __converters().toLocation(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.Location, but it was null.");
            } else {
              _tmpLocation = _tmp_1;
            }
            final ObjectType _tmpObjectType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfObjectType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfObjectType);
            }
            final ObjectType _tmp_3 = __converters().toObjectType(_tmp_2);
            if(_tmp_3 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.ObjectType, but it was null.");
            } else {
              _tmpObjectType = _tmp_3;
            }
            final PostType _tmpPostType;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfPostType)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfPostType);
            }
            _tmpPostType = __converters().toPostType(_tmp_4);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final Double _tmpReward;
            if (_cursor.isNull(_cursorIndexOfReward)) {
              _tmpReward = null;
            } else {
              _tmpReward = _cursor.getDouble(_cursorIndexOfReward);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _result = new Post(_tmpId,_tmpOwnerId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpObjectType,_tmpPostType,_tmpPhotoUri,_tmpReward,_tmpTimestamp);
          } else {
            _result = null;
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
  public Object getPostsForUser(final long ownerId,
      final Continuation<? super List<Post>> continuation) {
    final String _sql = "SELECT * FROM posts WHERE ownerId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ownerId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Post>>() {
      @Override
      public List<Post> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfObjectType = CursorUtil.getColumnIndexOrThrow(_cursor, "objectType");
          final int _cursorIndexOfPostType = CursorUtil.getColumnIndexOrThrow(_cursor, "postType");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfReward = CursorUtil.getColumnIndexOrThrow(_cursor, "reward");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Post _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOwnerId;
            _tmpOwnerId = _cursor.getLong(_cursorIndexOfOwnerId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Location _tmpLocation;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfLocation);
            }
            final Location _tmp_1 = __converters().toLocation(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.Location, but it was null.");
            } else {
              _tmpLocation = _tmp_1;
            }
            final ObjectType _tmpObjectType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfObjectType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfObjectType);
            }
            final ObjectType _tmp_3 = __converters().toObjectType(_tmp_2);
            if(_tmp_3 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.ObjectType, but it was null.");
            } else {
              _tmpObjectType = _tmp_3;
            }
            final PostType _tmpPostType;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfPostType)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfPostType);
            }
            _tmpPostType = __converters().toPostType(_tmp_4);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final Double _tmpReward;
            if (_cursor.isNull(_cursorIndexOfReward)) {
              _tmpReward = null;
            } else {
              _tmpReward = _cursor.getDouble(_cursorIndexOfReward);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Post(_tmpId,_tmpOwnerId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpObjectType,_tmpPostType,_tmpPhotoUri,_tmpReward,_tmpTimestamp);
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
  public Object getLostPostsWithOwnerInfo(final PostType lostType,
      final Continuation<? super List<PostDisplayDto>> continuation) {
    final String _sql = "\n"
            + "            SELECT p.*, u.username as ownerUsername, u.photoUri as ownerPhotoUri\n"
            + "            FROM posts p\n"
            + "            INNER JOIN users u ON p.ownerId = u.id\n"
            + "            WHERE p.postType = ? \n"
            + "            ORDER BY p.timestamp DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters().fromPostType(lostType);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PostDisplayDto>>() {
      @Override
      public List<PostDisplayDto> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfObjectType = CursorUtil.getColumnIndexOrThrow(_cursor, "objectType");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfReward = CursorUtil.getColumnIndexOrThrow(_cursor, "reward");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfOwnerUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerUsername");
          final int _cursorIndexOfOwnerPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerPhotoUri");
          final List<PostDisplayDto> _result = new ArrayList<PostDisplayDto>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PostDisplayDto _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOwnerId;
            _tmpOwnerId = _cursor.getLong(_cursorIndexOfOwnerId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Location _tmpLocation;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfLocation);
            }
            final Location _tmp_2 = __converters().toLocation(_tmp_1);
            if(_tmp_2 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.Location, but it was null.");
            } else {
              _tmpLocation = _tmp_2;
            }
            final ObjectType _tmpObjectType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfObjectType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfObjectType);
            }
            final ObjectType _tmp_4 = __converters().toObjectType(_tmp_3);
            if(_tmp_4 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.ObjectType, but it was null.");
            } else {
              _tmpObjectType = _tmp_4;
            }
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final Double _tmpReward;
            if (_cursor.isNull(_cursorIndexOfReward)) {
              _tmpReward = null;
            } else {
              _tmpReward = _cursor.getDouble(_cursorIndexOfReward);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpOwnerUsername;
            if (_cursor.isNull(_cursorIndexOfOwnerUsername)) {
              _tmpOwnerUsername = null;
            } else {
              _tmpOwnerUsername = _cursor.getString(_cursorIndexOfOwnerUsername);
            }
            final String _tmpOwnerPhotoUri;
            if (_cursor.isNull(_cursorIndexOfOwnerPhotoUri)) {
              _tmpOwnerPhotoUri = null;
            } else {
              _tmpOwnerPhotoUri = _cursor.getString(_cursorIndexOfOwnerPhotoUri);
            }
            _item = new PostDisplayDto(_tmpId,_tmpOwnerId,_tmpOwnerUsername,_tmpOwnerPhotoUri,_tmpTitle,_tmpDescription,_tmpLocation,_tmpObjectType,_tmpPhotoUri,_tmpReward,_tmpTimestamp);
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
  public Object getLostPosts(final PostType lostType,
      final Continuation<? super List<Post>> continuation) {
    final String _sql = "SELECT * FROM posts WHERE postType = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters().fromPostType(lostType);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Post>>() {
      @Override
      public List<Post> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfObjectType = CursorUtil.getColumnIndexOrThrow(_cursor, "objectType");
          final int _cursorIndexOfPostType = CursorUtil.getColumnIndexOrThrow(_cursor, "postType");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfReward = CursorUtil.getColumnIndexOrThrow(_cursor, "reward");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Post _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOwnerId;
            _tmpOwnerId = _cursor.getLong(_cursorIndexOfOwnerId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Location _tmpLocation;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfLocation);
            }
            final Location _tmp_2 = __converters().toLocation(_tmp_1);
            if(_tmp_2 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.Location, but it was null.");
            } else {
              _tmpLocation = _tmp_2;
            }
            final ObjectType _tmpObjectType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfObjectType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfObjectType);
            }
            final ObjectType _tmp_4 = __converters().toObjectType(_tmp_3);
            if(_tmp_4 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.ObjectType, but it was null.");
            } else {
              _tmpObjectType = _tmp_4;
            }
            final PostType _tmpPostType;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfPostType)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfPostType);
            }
            _tmpPostType = __converters().toPostType(_tmp_5);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final Double _tmpReward;
            if (_cursor.isNull(_cursorIndexOfReward)) {
              _tmpReward = null;
            } else {
              _tmpReward = _cursor.getDouble(_cursorIndexOfReward);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Post(_tmpId,_tmpOwnerId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpObjectType,_tmpPostType,_tmpPhotoUri,_tmpReward,_tmpTimestamp);
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
  public Object getLostPostsByFilter(final Location location, final ObjectType objectType,
      final PostType lostType, final Continuation<? super List<Post>> continuation) {
    final String _sql = "SELECT * FROM posts WHERE postType = ? AND (? IS NULL OR location = ?) AND (? IS NULL OR objectType = ?) ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    final String _tmp = __converters().fromPostType(lostType);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters().fromLocation(location);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    _argIndex = 3;
    final String _tmp_2 = __converters().fromLocation(location);
    if (_tmp_2 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_2);
    }
    _argIndex = 4;
    final String _tmp_3 = __converters().fromObjectType(objectType);
    if (_tmp_3 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_3);
    }
    _argIndex = 5;
    final String _tmp_4 = __converters().fromObjectType(objectType);
    if (_tmp_4 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_4);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Post>>() {
      @Override
      public List<Post> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfObjectType = CursorUtil.getColumnIndexOrThrow(_cursor, "objectType");
          final int _cursorIndexOfPostType = CursorUtil.getColumnIndexOrThrow(_cursor, "postType");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfReward = CursorUtil.getColumnIndexOrThrow(_cursor, "reward");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Post _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOwnerId;
            _tmpOwnerId = _cursor.getLong(_cursorIndexOfOwnerId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Location _tmpLocation;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfLocation);
            }
            final Location _tmp_6 = __converters().toLocation(_tmp_5);
            if(_tmp_6 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.Location, but it was null.");
            } else {
              _tmpLocation = _tmp_6;
            }
            final ObjectType _tmpObjectType;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfObjectType)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfObjectType);
            }
            final ObjectType _tmp_8 = __converters().toObjectType(_tmp_7);
            if(_tmp_8 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.ObjectType, but it was null.");
            } else {
              _tmpObjectType = _tmp_8;
            }
            final PostType _tmpPostType;
            final String _tmp_9;
            if (_cursor.isNull(_cursorIndexOfPostType)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getString(_cursorIndexOfPostType);
            }
            _tmpPostType = __converters().toPostType(_tmp_9);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final Double _tmpReward;
            if (_cursor.isNull(_cursorIndexOfReward)) {
              _tmpReward = null;
            } else {
              _tmpReward = _cursor.getDouble(_cursorIndexOfReward);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Post(_tmpId,_tmpOwnerId,_tmpTitle,_tmpDescription,_tmpLocation,_tmpObjectType,_tmpPostType,_tmpPhotoUri,_tmpReward,_tmpTimestamp);
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
  public Object getLostPostsByFilterWithOwnerInfo(final Location location,
      final ObjectType objectType, final PostType lostType,
      final Continuation<? super List<PostDisplayDto>> continuation) {
    final String _sql = "\n"
            + "        SELECT p.*, u.username as ownerUsername, u.photoUri as ownerPhotoUri\n"
            + "        FROM posts p\n"
            + "        INNER JOIN users u ON p.ownerId = u.id\n"
            + "        WHERE p.postType = ? \n"
            + "        AND (? IS NULL OR p.location = ?) \n"
            + "        AND (? IS NULL OR p.objectType = ?) \n"
            + "        ORDER BY p.timestamp DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    final String _tmp = __converters().fromPostType(lostType);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters().fromLocation(location);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    _argIndex = 3;
    final String _tmp_2 = __converters().fromLocation(location);
    if (_tmp_2 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_2);
    }
    _argIndex = 4;
    final String _tmp_3 = __converters().fromObjectType(objectType);
    if (_tmp_3 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_3);
    }
    _argIndex = 5;
    final String _tmp_4 = __converters().fromObjectType(objectType);
    if (_tmp_4 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_4);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PostDisplayDto>>() {
      @Override
      public List<PostDisplayDto> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOwnerId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfObjectType = CursorUtil.getColumnIndexOrThrow(_cursor, "objectType");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfReward = CursorUtil.getColumnIndexOrThrow(_cursor, "reward");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfOwnerUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerUsername");
          final int _cursorIndexOfOwnerPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerPhotoUri");
          final List<PostDisplayDto> _result = new ArrayList<PostDisplayDto>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PostDisplayDto _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOwnerId;
            _tmpOwnerId = _cursor.getLong(_cursorIndexOfOwnerId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Location _tmpLocation;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfLocation);
            }
            final Location _tmp_6 = __converters().toLocation(_tmp_5);
            if(_tmp_6 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.Location, but it was null.");
            } else {
              _tmpLocation = _tmp_6;
            }
            final ObjectType _tmpObjectType;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfObjectType)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfObjectType);
            }
            final ObjectType _tmp_8 = __converters().toObjectType(_tmp_7);
            if(_tmp_8 == null) {
              throw new IllegalStateException("Expected non-null com.example.lostpals.data.entity.ObjectType, but it was null.");
            } else {
              _tmpObjectType = _tmp_8;
            }
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final Double _tmpReward;
            if (_cursor.isNull(_cursorIndexOfReward)) {
              _tmpReward = null;
            } else {
              _tmpReward = _cursor.getDouble(_cursorIndexOfReward);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpOwnerUsername;
            if (_cursor.isNull(_cursorIndexOfOwnerUsername)) {
              _tmpOwnerUsername = null;
            } else {
              _tmpOwnerUsername = _cursor.getString(_cursorIndexOfOwnerUsername);
            }
            final String _tmpOwnerPhotoUri;
            if (_cursor.isNull(_cursorIndexOfOwnerPhotoUri)) {
              _tmpOwnerPhotoUri = null;
            } else {
              _tmpOwnerPhotoUri = _cursor.getString(_cursorIndexOfOwnerPhotoUri);
            }
            _item = new PostDisplayDto(_tmpId,_tmpOwnerId,_tmpOwnerUsername,_tmpOwnerPhotoUri,_tmpTitle,_tmpDescription,_tmpLocation,_tmpObjectType,_tmpPhotoUri,_tmpReward,_tmpTimestamp);
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
    return Arrays.asList(Converters.class);
  }

  private synchronized Converters __converters() {
    if (__converters == null) {
      __converters = __db.getTypeConverter(Converters.class);
    }
    return __converters;
  }
}
