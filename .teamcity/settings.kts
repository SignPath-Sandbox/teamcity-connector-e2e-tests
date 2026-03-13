import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.SignPathSubmitSigningRequestStep
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.buildSteps.signPathSubmitSigningRequest

version = "2025.11"

project {
  buildType {
    name = "Build and Sign"
    id("build_and_sign")

    vcs {
      root(DslContext.settingsRoot)
      cleanCheckout = true
    }

  params {
    text("connectorUrl", "https://teamcity-connector-stable.customersimulation.int.signpath.io")
    password("apiToken", "credentialsJSON:4ba5812a-ffd4-41a7-829c-15adcebda622")
  }

    val artifactPath = "BuildOutput.zip"

    // publish the signed artifact
    artifactRules = artifactPath

    steps {
      // build step
      script {
        name = "Create artifact"
        scriptContent = "./build.sh"
        formatStderrAsError = true
      }

      signPathSubmitSigningRequest {
        connectorUrl = "%connectorUrl"
        organizationId = "9ff791fc-c563-44e3-ab8c-86a33c910bbe"
        apiToken = "%apiToken%"
        projectSlug = "e2e-teamcity-test-project"
        signingPolicySlug = "test-signing"
        artifactConfigurationSlug = "initial"

        inputArtifactPath = artifactPath
        outputArtifactPath = artifactPath
        waitForCompletion = true
      }
    }

    // max build duration 5 mins
    failureConditions {
      executionTimeoutMin = 5
    }
  }
}
