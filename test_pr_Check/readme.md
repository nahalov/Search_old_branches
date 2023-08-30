1.Все yml файлы в определенной папке должны иметь уникальное имя в (поле name). В случаи несовпадения выводить сообщение об ошибке и информацию в каком файле некорректное имя.

2.Тест должен был отрабатывать локально. И применялся gradle (.gitignore, bulid.gradle, nexus.properties, settings.gradle).

                            Результат отработки скрипта. 


Найдены файлы: remoteSrc\conf\config\parameters\kksb_enigma.rmkib-leads-v2.cd.conf

Из файла kksb_enigma.rmkib-leads-v2.cd.conf: Значение rmkib-leads-v2 содержит deploymentUnit=

ott-egress-cm.${distrib.release.version} Не валидные имена remoteSrc\conf\openshift\bh-rmkib-leads-v2\istio\config\egress\ott-egress-cm.yaml

ott-jm-cm.${distrib.release.version} Не валидные имена remoteSrc\conf\openshift\bh-rmkib-leads-v2\istio\config\egress\ott-jour-mon-cm.yaml

istio-basic Не валидные имена remoteSrc\conf\openshift\bh-rmkib-leads-v2\istio\config\ingress\istio-basic-cm.yaml

sticky-dr-header Не валидные имена remoteSrc\conf\openshift\bh-rmkib-leads-v2\istio\config\ingress\sticky-dsr.yaml

mq.jks. Не валидные имена remoteSrc\conf\openshift\bh-rmkib-leads-v2\istio\secrets\secrets.yml

ise.keystores. Не валидные имена remoteSrc\conf\openshift\secrets\secrets.yml



Список должен быть пустым. Expression: blacklist.isEmpty()

java.lang.AssertionError: Список должен быть пустым. Expression: blacklist.isEmpty()