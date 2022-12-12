from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String

USER = "postgres"
PW = "" # Password
URL = "" # URL
PORT = "" # PORT
DB = "postgres"

engine = create_engine("postgresql://{}:{}@{}:{}/{}".format(USER, PW, URL,PORT, DB))
db_session = scoped_session(sessionmaker(autocommit=False, autoflush=False, bind=engine))

Base = declarative_base()
Base.query = db_session.query_property()

class User(Base):
    __tablename__ = 'players'
    id = Column(Integer, primary_key=True)
    player_id = Column(Integer, unique=True)
    position = Column(String(10), unique=False)
    type = Column(String(20), unique=False)

    def __init__(self, player_id=None, position=None, type=None):
        self.player_id = player_id
        self.position = position
        self.type = type

    def __repr__(self):
        return f'<Player {self.player_id!r}>'

# Base.metadata.drop_all(bind=engine)
Base.metadata.create_all(bind=engine)

from flask import Flask
from flask import request
from flask import jsonify
from werkzeug.serving import WSGIRequestHandler

import json
WSGIRequestHandler.protocol_version = "HTTP/1.1"

app = Flask(__name__)

@app.route("/add", methods=['POST'])
def add():
    content = request.get_json(silent=True, force=True)
    player_id = content["player_id"]
    position = content["position"]
    type = content["type"]

    if db_session.query(User).filter_by(player_id=player_id).first() is None:
        u = User(player_id=player_id, position=position, type=type)
        db_session.add(u)
        db_session.commit()
        return jsonify(success=True)
    
    else:
        return jsonify(success=False)

@app.route("/update", methods=['POST'])
def update():
    content = request.get_json(silent=True)
    prev = content["prev"]
    player_id = content["player_id"]

    user = db_session.query(User).filter_by(player_id=prev).first()
    new_user = db_session.query(User).filter_by(player_id=player_id).first()

    if user is not None and new_user is None:
        user.player_id = player_id
        db_session.commit()
        return jsonify(success=True)

    else:
        return jsonify(success=False)

@app.route("/reset", methods=['POST'])
def reset():
    content = request.get_json(silent=True)
    type = content["type"]

    db_session.query(User).filter_by(type=type).delete()
    db_session.commit()

    return jsonify()

@app.route("/load", methods=['POST'])
def load():
    content = request.get_json(silent=True)
    type = content["type"]

    players = db_session.query(User).filter_by(type=type)
    if players.first() is not None:
        players = players.all()
        return jsonify(success=True, results=[{"player_id": i.player_id, "position": i.position} for i in players])
            
    else:
        return jsonify(success=False)

if __name__ == "__main__":
    app.run(host='localhost', port=8888)
