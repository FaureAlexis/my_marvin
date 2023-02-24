folder('Tools') {
    job('clone-repository') {
        parameters {
            stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
        }
        steps {
            shell('git clone ${GIT_REPOSITORY_URL}')
        }
        wrappers {
            preBuildCleanup()
        }
    }
    job('SEED') {
        parameters {
            stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "EpitechIT31000/chocolatine")')
            stringParam('DISPLAY_NAME', '', 'Display name for the job')
        }
        steps {
            dsl {
                filename('job_dsl.groovy')
                additionalParameters([
                    'GITHUB_NAME': '${.GITHUB_NAME}',
                    'JOB_DISPLAY_NAME': '${params.DISPLAY_NAME}'
                ])
            }
        }
    }
}

class JobDslScript implements DSL {
    void apply(DslFactory factory) {
        def GITHUB_NAME = factory.stringParam('GITHUB_NAME', '')
        def JOB_DISPLAY_NAME = factory.stringParam('JOB_DISPLAY_NAME', '')
        factory.job(JOB_DISPLAY_NAME) {
            displayName(JOB_DISPLAY_NAME)
            wrappers {
                preBuildCleanup()
            }
            properties {
                githubProjectUrl('https://github.com/${GITHUB_NAME}')
            }
            scm {
                git {
                    remote {
                        url('${GITHUB_NAME}', 'main')
                    }
                }
            }
            triggers {
                scm('* * * * *')
            }
            steps {
                shell('make fclean')
                shell('make')
                shell('make tests_run')
                shell('make clean')
            }
        }
    }
}
