# baseball_roster
나만의 야구 선수단 만들기

## Tech Stack
Android, Java, Flask, Python, AWS Lambda, AWS RDS

## Design Pattern
MVP pattern
1. MainActivity – InitialPresenter – InitialModel
2. HitterActivity – HitterPresenter – HitterModel
3. PitcherActivity – PitcherPresenter – PitcherModel
4. SearchActivity – SearchPresenter - SearchModel
5. PlayerListActivity – PlayerListPresenter - PlayerListModel

## Description
### What is baseball_roster?
- 포지션 별로 메이저 리그의 선수들을 골라 자신만의 선수단을 꾸릴 수 있는 어플리케이션입니다.
- 선수단의 최대 인원은 25명으로 타자 13명(선발 9명, 후보 4명), 투수 12명(선발 투수 5명, 중계 및 마무리 투수(편의상 후보로 지칭) 7명)입니다.
- 현실에서는 서로 다른 팀에 속해 있더라도, 이 앱 안에서는 같은 팀으로 꾸릴 수 있습니다. 심지어, 이미 은퇴한 선수와 현역으로 뛰고 있는 선수도 같은 팀이 될 수 있습니다.
- 1998년부터 2022년까지의 각 구단의 로스터 검색이 가능합니다.
- 선수를 원래의 포지션과 동일한 포지션에 배치해야 한다는 제한사항이 있습니다.
- 예외적으로 지명타자는 모든 포지션의 타자를 배치할 수 있습니다. 투타겸업인 선수 또한 지명타자와 타자 후보에 배치가 가능합니다.
- 선택한 선수들은 AWS 서버에 저장됩니다. 따라서 어플리케이션을 종료했다가 다시 실행해도 서버로부터 선수 정보를 불러오기 때문에 화면이 유지가 됩니다.
- 선수 데이터는 MLB의 statsapi를 사용했습니다. 해당 API에는 종종 데이터가 누락된 선수가 있는데, 이런 선수들은 제외를 하였습니다. 선수들의 얼굴 사진은 MLB.com으로부터 불러와서 사용하였고, 기본 이미지가 뜬다면 MLB.com에 해당 선수 얼굴 사진이 등록되지 않은 것입니다.

### Activity
#### 1. MainActivity
어플리케이션의 초기 화면을 구성합니다. Hitter 버튼을 누르면 Hitter Roster 화면으로, Pitcher 버튼을 누르면 Pitcher Roster 화면으로 넘어갑니다.

<img src="https://user-images.githubusercontent.com/92534037/206984620-e1509b75-afc0-4a01-85d4-a0b2ae149145.png" width="30%"/>

#### 2. HitterRosterActivity
타자 선수진 화면을 구성합니다. 새로운 타자를 추가할 수도 있고, 초기화를 시킬 수도 있습니다.

<img src="https://user-images.githubusercontent.com/92534037/206985771-0032a4b1-11b1-47bd-8e39-85fb6c80d1f1.png" width="30%"/>

#### 3. PitcherRosterActivity
HitterRosterActivity의 투수 버전입니다. 기능은 동일합니다.

#### 4. SearchActivity
팀과 연도를 선택하여 검색을 할 수 있습니다. Search 버튼을 누르면 해당 연도의 해당 팀 선수진 목록이 나오게 됩니다. HitterRosterActivity에서 포수 추가 버튼을 눌러서 SearchActivity로 넘어왔다면 Search의 결과는 포수 선수들만 나오게 됩니다. 지명타자나 타자 후보의 경우 모든 포지션의 타자가 모두 가능하기 때문에 모든 타자 선수들이 나옵니다.

<img src="https://user-images.githubusercontent.com/92534037/206986122-879fc031-cf14-4f9c-bd0f-fe2824a027d7.png" width="30%"/>

#### 5. PlayListActivity
검색한 결과의 선수들이 리스트뷰에 나타납니다. 각 리스트는 선수의 얼굴 사진과 해당 선수의 간략한 기록들로 이루어져 있습니다. 한 선수를 선택하여 자신의 선수로 만들 수 있습니다.

<img src="https://user-images.githubusercontent.com/92534037/206986229-8f2aa515-1f60-4003-8496-ab8ae0534de2.png" width="30%"/>
<br/>

### After select!!!
<img src="https://user-images.githubusercontent.com/92534037/206986963-67278dfb-031c-4c2e-bcaf-98274eb001df.png" width="30%"/>

