pipelineJob('osc-qa-tina_redwires') {
  properties{
    disableConcurrentBuilds()
  	disableResume()
  }
  logRotator(90, -1, 1, -1)
  parameters {
    stringParam('Venv_Path', '', 'Virtual environment path where osc-qa-tina-redwires has been installed.')
    stringParam('Region', '', 'Target region for tests.')
    stringParam('Account', '', 'Used account for tests.')
    stringParam('Nb_Workers', '', 'Number of pytest workers (increase this value reduce runtime, but account need more quotas).')
    extendedChoice{
      name('Tests')
      description('Selection of executed tests.    -    FUNCTIONAL_PUBLIC will execute all PUBLIC sub-tests like VOLUME, SNAPSHOT...    -    FUNCTIONAL_NET will execute all NET sub-tests like NAT, VPN...')
      type('PT_CHECKBOX')
      saveJSONParameterToFile(false)
      quoteValue(false)
      visibleItemCount(30)
      multiSelectDelimiter(',')
      value("API_DIRECTLINK,API_EIM,API_FCU,API_ICU,API_LBU,API_OAPI,API_OOS,FUNCTIONAL_PUBLIC,API_LOG,FGPU,IMAGE,LOAD_BALANCER,QUOTA,SNAPSHOT,SUB_REGION,VOLUME,FUNCTIONAL_NET,DHCP_OPTION,NAT,PEERING,NET_ACCESS_POINT,NIC,VPN")
      defaultValue("API_DIRECTLINK,API_EIM,API_FCU,API_ICU,API_LBU,API_OAPI,API_OOS,FUNCTIONAL_PUBLIC,FUNCTIONAL_NET")
      projectName('')
      propertyFile('')
      groovyScript('')
      groovyScriptFile('')
      bindings('')
      groovyClasspath('')
      propertyKey('')
      defaultPropertyFile('')
      defaultGroovyScript('')
      defaultGroovyScriptFile('')
      defaultBindings('')
      defaultGroovyClasspath('')
      defaultPropertyKey('')
      descriptionPropertyValue('')
      descriptionPropertyFile('')
      descriptionGroovyScript('')
      descriptionGroovyScriptFile('')
      descriptionBindings('')
      descriptionGroovyClasspath('')
      descriptionPropertyKey('')
      javascriptFile('')
      javascript('')
  	}
  }
  definition {
    cps {
      sandbox()
      script("""node {
    stage('Clean Accounts') {
        sh \"\"\"set +e
            source \\\${Venv_Path}/bin/activate
            osc-qa-tina-redwires -r \\\${Region} -a \\\${Account} --clean
        \"\"\"
    }
    stage('Check clean Accounts') {
        sh \"\"\"set +e
            source \\\${Venv_Path}/bin/activate
            osc-qa-tina-redwires -r \\\${Region} -a \\\${Account} --verify-clean
        \"\"\"
    }
    stage('Exec tests') {
        try {
            sh \"\"\"set +e
                source \\\${Venv_Path}/bin/activate
                osc-qa-tina-redwires -r \\\${Region} -a \\\${Account} -n \\\${Nb_Workers} -l ./log.txt --junit ./junit.xml --doc ./report.html -t \\\$(echo \\\${Tests} | tr ',' ' ')
                ret=\\\$?
                sed -i 's/classname="[0-9a-z_.-]*\\\\.qa_tina_redwires/classname="qa_tina_redwires/g' ./junit.xml
                exit \\\${ret}
            \"\"\"
        }
        finally {
            junit 'junit.xml'
            publishHTML (target: [
                allowMissing: false,
                alwaysLinkToLastBuild: false,
                keepAll: true,
                reportDir: './',
                reportFiles: 'report.html',
                reportName: "Report"
            ])
        }
    }
    stage('Check clean Accounts') {
        sh \"\"\"set +e
            source \\\${Venv_Path}/bin/activate
            osc-qa-tina-redwires -r \\\${Region} -a \\\${Account} --verify-clean
        \"\"\"
    }
}""")
    }
  }
}