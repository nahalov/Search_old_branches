import groovy.json.JsonSlurper

import java.time.*

import java.util.*

import java.text.SimpleDateFormat



pipeline {

    options {

        skipDefaultCheckout()

    }

    agent {

        node {

            //label 'Linux_Default'

            label "${env.NODE}"

            //Сборка должна происходить на централизованных агентах из пула Linux_Default





        }

    }

    stages {

        stage('CleanWorkspace and Checkout') {

            steps {

                cleanWs disableDeferredWipeout: true, deleteDirs: true

                checkout scm

            }

        }



        stage('CollectionData') {

            steps {

                script {

                    def inner_repo_ssh = "ssh://git@stash.delta.sbrf.ru:7999/rmkib/kksb_enigma_configs.git"

                    dir("CollectionData") {

                        git credentialsId: 'CI_Devops_Delta_SSH', url: inner_repo_ssh, branch: 'master'

                        def branches = sh(returnStdout: true, script: "git branch --all | grep -v 'module/' | grep -v '/release/' | grep -v 'common/' | grep -v 'release/'")

                        def array = branches.split("\n")

                        def calendar = Calendar.getInstance()

                        calendar.add(Calendar.DAY_OF_MONTH, -21)



                        def out_of_date = calendar.getTimeInMillis()

                        printColoredMessage("Выгрузка веток где коммит больше 3 недель", "green")

                        printColoredMessage("Удалены будут  ветки старше " + new Date(out_of_date).toString(), "yellow")

                        def branchesToExclude =["develop", "master", "remotes/origin/HEAD -> origin/master","remotes/origin/master", "* master"]

                        def count = 0

                        for (branch in array) {

                            branch = branch.trim()

                            if (!(branchesToExclude.contains(branch))){

                                def date_from_commit = sh(returnStdout: true, script: "git log -1  ${branch} --pretty='%ad' --date=format:'%Y-%m-%d %H:%M:%S'")

                                def author = sh(returnStdout: true, script: "git log -1 ${branch} --pretty=format:'%an %ce'")

                                printColoredMessage("${branch} \"${author}", "green")

                                def date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date_from_commit)

                                        .getTime()

                                if (date < out_of_date) {

                                    println(branch + ": " + new Date(date).toString())

                                    sh(script: "echo -e \"\\012\" $branch + $author >> file.txt")

                                    count++

                                }

                            }

                        }



                        printColoredMessage(count + " веток будет удалено", "yellow")

                    }

                }

            }

        }

        stage('Output result') {

            steps {

                dir("CollectionData") {

                    archiveArtifacts artifacts: 'file.txt', onlyIfSuccessful: false

                    sh(script: "nl file.txt")

                }

            }

        }

    }

}



def printColoredMessage(String message, String color = 'black') {

    def colors = ['black'  : '030',

                  'red'    : '031',

                  'green'  : '032',

                  'yellow' : '033',

                  'blue'   : '034',

                  'magenta': '035',

                  'cyan'   : '036',

                  'white'  : '037']

    ansiColor("xterm") {

        echo "\033[${colors."${color?.toLowerCase()}" ?: colors.black}m${message ?: ''}\033[0m"

    }

}

