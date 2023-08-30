import jdk.internal.org.jline.reader.SyntaxError
import org.apache.groovy.yaml.util.YamlConverter
import org.junit.Test
import static org.junit.Assert.*

import groovy.io.FileType
import groovy.yaml.YamlSlurper
import org.opentest4j.AssertionFailedError

class check_yaml_name {

    @Test
    void check_yaml_names() {
        def directoryPath = "remoteSrc/conf/config/parameters/"
        def extension = "cd.conf"
        def checkparam = "deploymentUnit="
        def value

        // Создаем объект File для указанной директории
        def directory = new File(directoryPath)

        // Получаем список всех файлов с расширением .conf
        def confFiles = directory.listFiles().findAll { file ->
            file.isFile() && file.name.endsWith(extension)
        }
        // Выводим найденные файлы .conf

        confFiles.each { file ->
            println("Найдены файлы: ${file}")
        }
        // Проходимся по каждому файлу и ищем значения с ключевым словом «checkparam»

        confFiles.each { file ->
            try {
                file.withReader { reader ->
                    reader.eachLine { line ->
                        if (line.contains(checkparam)) {
                            def keyValue = line.split("=")
                            if (keyValue.size() >= 2) {
                                value = keyValue[1].trim() replaceAll("[\\._]", "-")
                                println("Из файла ${file.name}: Значение $value содержит $checkparam")
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                println("Ошибка: Файл ${file.name} не найден.")
                throw e
            } catch (Exception e) {
                println("Ошибка при чтении файла ${file.name}: " + e.message)
                throw e
            }
        }


        def filePathsForExclude = ["remoteSrc/conf/openshift/"]

        def folderPath = "remoteSrc/conf/openshift"
        def list = []
        def dir = new File(folderPath)
        dir.eachFileRecurse(FileType.FILES) { file ->
            if (file.isFile()
                    && (file.name.endsWith(".yaml") || file.name.endsWith(".yml"))
                    && !filePathsForExclude.contains(file.getPath())) {
                list.add(file)
            }
        }
        def fileNames = []
        def map = [:]
        for (filename in list) {
            def content = ""
            filename.eachLine { line ->
                content = content + "\n" + line.replaceAll(/\{[\{\%]{1}.*[\}\%]{1}\}/, "")
            }
            def ymldata = new YamlSlurper().parseText(content)
            fileNames.add(ymldata.metadata.name)
            map.put(ymldata.metadata.name, filename)
        }

        def blacklist =  []
        for (fileName in fileNames) {
            if (fileName.contains(value) || fileName.startsWith("\${")) {
            } else {

                blacklist.add(fileName)
            }
        }
        blacklist.each { println(it + " Не валидные имена " + map[it]) }
        assert blacklist.isEmpty() : "Список должен быть пустым"
    }
}