echo 'qiang'|sudo -S camvi_face_engine stop
sleep 5s
sudo service tomcat8 stop
mysql -u camvi -pcamvi --execute="drop database camvidb;"
echo "drop db"
gnome-terminal -x bash -c "./startTomcat.sh"
sleep 10s
echo "open another terminal and start tomcat"
mysql -u camvi -pcamvi<<EOF
use camvidb;
source /usr/lib/camvi/create_mysql_triggers.sql
quit
EOF
sleep 5s
echo 'qiang'|sudo -S camvi_face_engine run 
sleep 10s
